package view;

import app.AppBuilder;

import javax.swing.*;
import java.awt.*;
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

public class StudyMetricsView extends View{
//    private final MetricsViewModel viewModel;
//    private final ViewStudyMetricsController controller;

    public StudyMetricsView() {
        super("studyMetrics");

        JPanel header = new ViewHeader("Study Metrics");
        this.add(header, BorderLayout.NORTH);

        JPanel subheading = new JPanel();
        JButton lastWeekButton = new JButton("< Last Week");
        JButton nextWeekButton = new JButton("Next Week >");

        subheading.add(lastWeekButton, BorderLayout.WEST);
        subheading.add(nextWeekButton, BorderLayout.EAST);
        this.add(subheading, BorderLayout.CENTER);

        JPanel main = new JPanel();

        main.setLayout(new BorderLayout());

        // DATASET 1 — Study duration (left axis)
        DefaultCategoryDataset leftDataset = new DefaultCategoryDataset();

//        Map<String, String> dailyData = viewModel.getDailyStudyDurations();
//        for (Map.Entry<String, String> entry : dailyData.entrySet()) {}
//        double hour = (double) duration.toMinutes() / 60;

        leftDataset.addValue(0.5, "Study Duration", "Sun");
        leftDataset.addValue(1, "Study Duration", "Mon");
        leftDataset.addValue(5, "Study Duration", "Tue");
        leftDataset.addValue(2, "Study Duration", "Wed");
        leftDataset.addValue(2, "Study Duration", "Thu");
        leftDataset.addValue(0, "Study Duration", "Fri");
        leftDataset.addValue(1.5, "Study Duration", "Sat");

        JFreeChart chart = ChartFactory.createLineChart(
                "Week of yyyy-mm-dd to yyyy-mm-dd",
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
        rightDataset.addValue(80, "Quiz Score", "Sun");
        rightDataset.addValue(70, "Quiz Score", "Mon");
        rightDataset.addValue(80, "Quiz Score", "Tue");
        rightDataset.addValue(90, "Quiz Score", "Wed");
        rightDataset.addValue(75, "Quiz Score", "Thu");
        rightDataset.addValue(0, "Quiz Score", "Fri");
        rightDataset.addValue(98, "Quiz Score", "Sat");

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


        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 450));

        main.add(chartPanel, BorderLayout.CENTER);

        JPanel returnPanel = new JPanel();
        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("dashboard");
        });
        returnPanel.add(returnButton);

        main.add(returnPanel, BorderLayout.SOUTH);
        this.add(main, BorderLayout.SOUTH);

    }
}
