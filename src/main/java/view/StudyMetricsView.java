package view;

import app.AppBuilder;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
// JfreeChart to create a dual axis line chart
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import interface_adapter.controller.ViewStudyMetricsController;
import interface_adapter.view_model.MetricsViewModel;

public class StudyMetricsView extends View implements PropertyChangeListener {
    private final MetricsViewModel viewModel;
    private final ViewStudyMetricsController controller;

    private ChartPanel chartPanel;
    private JPanel main;
    private int weekOffset = 0;

    public StudyMetricsView(MetricsViewModel viewModel, ViewStudyMetricsController controller) {
        super("studyMetrics");
        this.viewModel = viewModel;
        this.controller = controller;

        viewModel.addPropertyChangeListener(this);

        JPanel header = new ViewHeader("Study Metrics");
        this.add(header, BorderLayout.NORTH);

        JPanel subheading = new JPanel();
        JButton lastWeekButton = new JButton("< Last Week");
        JButton nextWeekButton = new JButton("Next Week >");

        lastWeekButton.addActionListener(e -> {
            weekOffset -= 1;
            loadMetrics();
        });

        nextWeekButton.addActionListener(e -> {
            weekOffset += 1;
            loadMetrics();
        });

        subheading.add(lastWeekButton);
        subheading.add(nextWeekButton);
        this.add(subheading, BorderLayout.CENTER);

        main = new JPanel();
        main.setLayout(new BorderLayout());

        // Create initial chart with data from viewModel
        updateChart();

        JPanel returnPanel = new JPanel();
        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("dashboard");
        });
        returnPanel.add(returnButton);

        main.add(returnPanel, BorderLayout.SOUTH);
        this.add(main, BorderLayout.SOUTH);

        // Load metrics when view is created
        loadMetrics();
    }

    private void loadMetrics() {
        // Trigger the use case to load metrics
        String userId = "user123"; // TODO: Get from session
        String courseId = "all"; // TODO: Get from UI selection
        String timeFilter = "week"; // TODO: fix time filter lol

        controller.execute(userId, courseId, timeFilter);
    }

    /**
     * Calculates the start date (Sunday) for a given week offset.
     */
    private LocalDateTime getStartDateForOffset(int offset) {
        LocalDateTime now = LocalDateTime.now();

        // Find the start of the current week (Sunday)
        int dayOfWeek = now.getDayOfWeek().getValue(); // 1 (Monday) to 7 (Sunday)
        int daysToSubtract = (dayOfWeek == 7) ? 0 : dayOfWeek; // If Sunday, don't subtract

        LocalDateTime startOfThisWeek = now.minusDays(daysToSubtract)
                .withHour(0)
                .withMinute(0)
                .withSecond(0);

        // Apply the offset
        return startOfThisWeek.plusWeeks(offset);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        // Update chart when daily study durations or course scores change
        if ("dailyStudyDurations".equals(propertyName) || "courseScores".equals(propertyName)) {
            updateChart();
        }

        // Handle error messages
        if ("errorMessage".equals(propertyName)) {
            String error = viewModel.getErrorMessage();
            if (!error.isEmpty()) {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateChart() {
        // Remove old chart if it exists
        if (chartPanel != null) {
            main.remove(chartPanel);
        }

        // DATASET 1 — Study duration (left axis)
        DefaultCategoryDataset leftDataset = new DefaultCategoryDataset();

        Map<String, Duration> dailyData = viewModel.getDailyStudyDurations();

        String[] daysOrder = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOrder) {
            Duration duration = dailyData.getOrDefault(day, Duration.ZERO);
            double hours = (double) duration.toMinutes() / 60;
            leftDataset.addValue(hours, "Study Duration", day);
        }
        //TODO: update the date
        LocalDateTime startDate = getStartDateForOffset(weekOffset);
         String dateRange = formatDateRange(startDate);

        JFreeChart chart = ChartFactory.createLineChart(
                dateRange,
                "",
                "Hours Studied",
                leftDataset
        );

        CategoryPlot plot = chart.getCategoryPlot();

        // Custom renderer for left dataset
        LineAndShapeRenderer leftRenderer = new LineAndShapeRenderer();
        leftRenderer.setSeriesShapesVisible(0, true);
        leftRenderer.setSeriesPaint(0, Color.RED);
        plot.setRenderer(0, leftRenderer);

        // DATASET 2 — Quiz score (right axis)
        DefaultCategoryDataset rightDataset = new DefaultCategoryDataset();

        // Get quiz scores from viewModel
        Map<String, String> courseScores = viewModel.getCourseScores();
        for (String day : daysOrder) {
            String scoreStr = courseScores.getOrDefault(day, "0%");
            // Parse the percentage string (e.g., "80.0%")
            double score = parseScore(scoreStr);
            rightDataset.addValue(score, "Quiz Score", day);
        }

        // Add the second dataset
        plot.setDataset(1, rightDataset);

        // Create and attach right-side axis
        NumberAxis rightAxis = new NumberAxis("Quiz Score (%)");
        plot.setRangeAxis(1, rightAxis);

        // Map dataset 2 → right axis
        plot.mapDatasetToRangeAxis(1, 1);

        // Renderer for dataset 2
        LineAndShapeRenderer rightRenderer = new LineAndShapeRenderer();
        rightRenderer.setSeriesShapesVisible(0, true);
        rightRenderer.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(1, rightRenderer);

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 450));

        main.add(chartPanel, BorderLayout.CENTER);

        // Refresh the panel
        main.revalidate();
        main.repaint();
    }

    /**
     * Helper method to parse score strings like "80.0%" into doubles.
     */
    private double parseScore(String scoreStr) {
        try {
            // Remove the % sign and parse
            return Double.parseDouble(scoreStr.replace("%", "").trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Helper method to format date range for chart title.
     */
    private String formatDateRange(LocalDateTime startDate) {
        LocalDateTime endDate = startDate.plusDays(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "Week of " + startDate.format(formatter) + " to " + endDate.format(formatter);
    }
}