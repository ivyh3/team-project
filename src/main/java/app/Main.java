package app;

import javax.swing.*;
import java.io.IOException;

/**
 * Main entry point for the AI Study Companion application.
 */
public class Main {
    public static void main(String[] args) {
        System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");
        // Initialize Firebase
        try {
            Config.initializeFirebase();
        } catch (IOException e) {
            System.err.println("Failed to initialize Firebase: " + e.getMessage());
            System.err.println("Please ensure firebase config json is in the correct location.");
            System.exit(1);
        }

        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Start application on EDT
        SwingUtilities.invokeLater(() -> {
            // // TODO: Initialize dependencies (repositories, services, interactors)
            // // TODO: Set up dependency injection
            // // TODO: Show login view first
            //
            // // For now, just show a placeholder window
            // JFrame frame = new JFrame("AI Study Companion");
            // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // frame.setSize(800, 600);
            //
            // JLabel placeholder = new JLabel("Welcome to AI Study Companion",
            // SwingConstants.CENTER);
            // placeholder.setFont(placeholder.getFont().deriveFont(24f));
            // frame.add(placeholder);
            //
            // frame.setLocationRelativeTo(null);
            // frame.setVisible(true);

            // Now actual code
            AppBuilder appBuilder = new AppBuilder();
            JFrame app = appBuilder
                    .addInitialView()
                    .addSignupView()
                    .addLoginView()
                    .addDashboardView()
                    .addSignupUseCase()
                    .addLoginUseCase()
                    // TODO: Sort out later.
                    .addSettingsView()
                    // .addChangePasswordUseCase()
                    // .addLogoutUseCase()
                    // .addChooseStudySessionView()
                    .addUploadSessionMaterialsView()
                    .addStudySessionView()
                    .addStudySessionEndView()
                    .addEndStudySessionUseCase()
                    .addVariableSessionView()
                    .addStudyQuizView()
                    .addFileManagerView()
                    .addQuizHistoryView()
                    .addScheduleSessionView()
                    .addStudyMetricsView()
                    .addStudySessionConfigView()
                    .addConfigStudySessionUseCase()
                    .addUploadMaterialsView()
                    .build();

            app.pack();
            app.setLocationRelativeTo(null);
            app.setVisible(true);
        });
    }
}