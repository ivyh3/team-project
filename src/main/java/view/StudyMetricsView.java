package view;

import app.AppBuilder;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
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
    private LocalDateTime startDate;

    public StudyMetricsView(MetricsViewModel viewModel, ViewStudyMetricsController controller) {
        super("studyMetrics");
        this.viewModel = viewModel;
        this.controller = controller;

        int daysSinceSunday = LocalDateTime.now().getDayOfWeek().getValue() % 7;
        this.startDate = LocalDateTime.now().minusDays(daysSinceSunday).toLocalDate().atStartOfDay();

        viewModel.addPropertyChangeListener(this);


        JPanel header = new ViewHeader("Study Metrics");
        this.add(header, BorderLayout.NORTH);

        JPanel subheading = new JPanel();
        JButton lastWeekButton = new JButton("< Last Week");
        JButton nextWeekButton = new JButton("Next Week >");

        lastWeekButton.addActionListener(e -> {
            startDate = startDate.minusDays(7);
            loadMetrics();
        });

        nextWeekButton.addActionListener(e -> {
            startDate = startDate.plusDays(7);
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

        SwingUtilities.invokeLater(this::loadMetrics); // TODO: no clue why but this doesn't load until the last week/
                                                        // next week buttons are pressed

    }

    public void loadMetrics() {controller.execute(startDate);}

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        if ("dailyStudyDurations".equals(propertyName) || "averageQuizScores".equals(propertyName)) {
            updateChart();
        }

        if ("startDate".equals(propertyName)) {
            this.startDate = viewModel.getStartDate();
            updateChart();
        }

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

        // Study duration (left axis)
        DefaultCategoryDataset leftDataset = new DefaultCategoryDataset();

        Map<DayOfWeek, Duration> dailyData = viewModel.getDailyStudyDurations();

        DayOfWeek[] days = {DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};
        for (DayOfWeek day : days) {
            Duration duration = dailyData.getOrDefault(day, Duration.ZERO);
            double hours = (double) duration.getSeconds() / 3600;
            leftDataset.addValue(hours, "Study Duration", day);
        }
        String dateRange = formatDateRange(startDate);

        JFreeChart chart = ChartFactory.createLineChart(
                dateRange,
                "",
                "Hours Studied",
                leftDataset
        );

        CategoryPlot plot = chart.getCategoryPlot();

        LineAndShapeRenderer leftRenderer = new LineAndShapeRenderer();
        leftRenderer.setSeriesShapesVisible(0, true);
        leftRenderer.setSeriesPaint(0, Color.RED);
        plot.setRenderer(0, leftRenderer);

        // Quiz score (right axis)
        DefaultCategoryDataset rightDataset = new DefaultCategoryDataset();

        Map<DayOfWeek, Float> quizScores = viewModel.getAverageQuizScores();
        for (DayOfWeek day : days) {
            Float score = quizScores.getOrDefault(day, 0f);
            rightDataset.addValue(score, "Quiz Score", day);
        }

        plot.setDataset(1, rightDataset);
        NumberAxis rightAxis = new NumberAxis("Quiz Score (%)");
        plot.setRangeAxis(1, rightAxis);
        plot.mapDatasetToRangeAxis(1, 1);
        LineAndShapeRenderer rightRenderer = new LineAndShapeRenderer();
        rightRenderer.setSeriesShapesVisible(0, true);
        rightRenderer.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(1, rightRenderer);

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 450));

        main.add(chartPanel, BorderLayout.CENTER);

        main.revalidate();
        main.repaint();
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