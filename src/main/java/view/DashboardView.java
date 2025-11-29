package view;

import javax.swing.*;
import java.awt.*;

import app.AppBuilder;
import interface_adapter.view_model.DashboardState;
import interface_adapter.view_model.DashboardViewModel;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends StatefulView<DashboardState> {
    private final JLabel stateLabel = new JLabel();

    public DashboardView(DashboardViewModel dashboardViewModel) {
        super("dashboard", dashboardViewModel);

        JPanel viewHeader = new ViewHeader("Dashboard");
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        final JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e -> {
            // Should use a controller for this shit instead, but not going to do that for
            // testing.
            AppBuilder.viewManagerModel.setView("settings");
        });
        viewHeader.add(settingsButton, BorderLayout.EAST);

        // ====== BUTTONS =====
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new GridLayout(2, 2, 20, 20));

        final JButton reviewQuizzesButton = new JButton("Review Quizzes");
        final JButton startStudyingButton = new JButton("Start Studying");
        final JButton scheduleStudySessionButton = new JButton("Schedule Session");
        final JButton viewStatisticsButton = new JButton("View Statistics");
        reviewQuizzesButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("quizHistory");
        });
        startStudyingButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("studySessionConfig");
        });
        scheduleStudySessionButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("scheduleSession");
        });
        viewStatisticsButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("studyMetrics");
        });

        buttonContainer.add(startStudyingButton);
        buttonContainer.add(reviewQuizzesButton);
        buttonContainer.add(scheduleStudySessionButton);
        buttonContainer.add(viewStatisticsButton);

        // ===== INSIGHTS TEXT ======
        JPanel insightsContainer = new JPanel();
        insightsContainer.setLayout(new BoxLayout(insightsContainer, BoxLayout.Y_AXIS));

        JLabel insightText = new JLabel("You did not study today yet. Start studying now!");
        // stateLabel.setText(String.format("You have studied %.1f hours today.",
        // viewModel.getState()));
        stateLabel.setText("You have studied 2.7 hours today.");

        insightText.setAlignmentX(Component.CENTER_ALIGNMENT);
        stateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        insightText.setFont(new Font(null, Font.BOLD, 18));
        stateLabel.setFont(new Font(null, Font.BOLD, 16));

        insightsContainer.add(insightText);
        insightsContainer.add(stateLabel);

        // Building Main
        main.add(insightsContainer);
        main.add(Box.createVerticalGlue()); // Split them vertically further
        main.add(buttonContainer);

        this.add(viewHeader, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
    }

    // @Override
    // public void propertyChange(PropertyChangeEvent evt) {
    // if (evt.getPropertyName().equals("state")) {
    // System.out.println(this.viewModel.getState());
    // this.stateLabel.setText(String.format("You have studied %f hours today.",
    // viewModel.getState()));
    // }
    // }
}