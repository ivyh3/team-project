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

import interface_adapter.view_model.DashboardViewModel;
import org.jetbrains.annotations.NotNull;
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
    public static final int DAYS_IN_A_WEEK = 7;
    public static final int SECONDS_IN_AN_HOUR = 3600;
    public static final int WIDTH = 350;
    public static final int HEIGHT = 450;
    public static final float OPACITY = 0.5f;
    public static final double PERFECT_SCORE = 100.0;

    private final MetricsViewModel viewModel;
    private final ViewStudyMetricsController controller;
    private final DashboardViewModel dashboardViewModel;

    private ChartPanel chartPanel;
    private final JPanel main;
    private LocalDateTime startDate;

    public StudyMetricsView(MetricsViewModel viewModel, ViewStudyMetricsController controller,
                            DashboardViewModel dashboardViewModel) {
        super("studyMetrics");
        this.viewModel = viewModel;
        this.controller = controller;
        this.dashboardViewModel = dashboardViewModel;

        final int daysSinceSunday = LocalDateTime.now().getDayOfWeek().getValue() % 7;
        this.startDate = LocalDateTime.now().minusDays(daysSinceSunday).toLocalDate().atStartOfDay();

        viewModel.addPropertyChangeListener(this);

        final JPanel header = new ViewHeader("Study Metrics");
        this.add(header, BorderLayout.NORTH);

        final JPanel subheading = getWeekPanel();
        this.add(subheading, BorderLayout.CENTER);

        main = new JPanel();
        main.setLayout(new BorderLayout());
        updateChart();

        final JPanel returnPanel = new JPanel();
        final JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> AppBuilder.viewManagerModel.setView("dashboard"));
        returnPanel.add(returnButton);

        main.add(returnPanel, BorderLayout.SOUTH);
        this.add(main, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(this::loadMetrics);
    }

    @NotNull
    private JPanel getWeekPanel() {
        final JPanel result = new JPanel();
        final JButton lastWeekButton = new JButton("< Last Week");
        final JButton nextWeekButton = new JButton("Next Week >");

        lastWeekButton.addActionListener(e -> {
            startDate = startDate.minusDays(DAYS_IN_A_WEEK);
            loadMetrics();
        });

        nextWeekButton.addActionListener(e -> {
            startDate = startDate.plusDays(DAYS_IN_A_WEEK);
            loadMetrics();
        });

        result.add(lastWeekButton);
        result.add(nextWeekButton);
        return result;
    }

    /**
     * Helper method to load metrics based on the start date of the week.
     */
    public void loadMetrics() {
        final String userId = dashboardViewModel.getState().getUserId();
        controller.execute(userId, startDate);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String propertyName = evt.getPropertyName();

        if ("dailyStudyDurations".equals(propertyName) || "averageQuizScores".equals(propertyName)) {
            updateChart();
        }
        if ("startDate".equals(propertyName)) {
            this.startDate = viewModel.getStartDate();
            updateChart();
        }
        if ("errorMessage".equals(propertyName)) {
            final String error = viewModel.getErrorMessage();
            if (!error.isEmpty()) {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Helper method to create the chart with the proper data.
     */
    private void updateChart() {
        if (chartPanel != null) {
            main.remove(chartPanel);
        }

        // study durations
        final DefaultCategoryDataset leftDataset = new DefaultCategoryDataset();

        final Map<DayOfWeek, Duration> dailyData = viewModel.getDailyStudyDurations();

        final DayOfWeek[] days = { DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY };
        for (DayOfWeek day : days) {
            final Duration duration = dailyData.getOrDefault(day, Duration.ZERO);
            final double hours = (double) duration.getSeconds() / SECONDS_IN_AN_HOUR;
            leftDataset.addValue(hours, "Study Duration", day);
        }
        final String dateRange = formatDateRange();

        final JFreeChart chart = ChartFactory.createLineChart(
                dateRange,
                "",
                "Hours Studied",
                leftDataset);

        final CategoryPlot plot = chart.getCategoryPlot();
        final NumberAxis leftAxis = (NumberAxis) plot.getRangeAxis();
        leftAxis.setAutoRange(true);
        leftAxis.setLowerBound(0.0);

        final LineAndShapeRenderer leftRenderer = new LineAndShapeRenderer();
        leftRenderer.setSeriesShapesVisible(0, true);
        leftRenderer.setSeriesPaint(0, new Color(1.0f, 0.0f, 0.0f, OPACITY));
        leftRenderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(0, leftRenderer);

        // quiz scores
        final DefaultCategoryDataset rightDataset = new DefaultCategoryDataset();

        final Map<DayOfWeek, Float> quizScores = viewModel.getAverageQuizScores();
        for (DayOfWeek day : days) {
            final Float score = quizScores.getOrDefault(day, 0f);
            rightDataset.addValue(score, "Quiz Score", day);
        }

        plot.setDataset(1, rightDataset);
        final NumberAxis rightAxis = new NumberAxis("Quiz Score (%)");
        rightAxis.setRange(0.0, PERFECT_SCORE);
        plot.setRangeAxis(1, rightAxis);
        plot.mapDatasetToRangeAxis(1, 1);
        final LineAndShapeRenderer rightRenderer = new LineAndShapeRenderer();
        rightRenderer.setSeriesShapesVisible(0, true);
        rightRenderer.setSeriesPaint(0, new Color(0.0f, 0.0f, 1.0f, OPACITY));
        rightRenderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(1, rightRenderer);

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        main.add(chartPanel, BorderLayout.CENTER);

        main.revalidate();
        main.repaint();
    }

    /**
     * Helper method to format date range for chart title.
     * 
     * @return the date range formatted as a string
     */
    private String formatDateRange() {
        final LocalDateTime endDate = startDate.plusDays(6);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "Week of " + startDate.format(formatter) + " to " + endDate.format(formatter);
    }
}