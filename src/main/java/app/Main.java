package app;

import javax.swing.*;

/**
 * Main entry point for the AI Study Companion application.
 */
public class Main {
	public static void main(String[] args) {
		// Set look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Start application on EDT
		SwingUtilities.invokeLater(() -> {
//			// TODO: Initialize dependencies (repositories, services, interactors)
//			// TODO: Set up dependency injection
//			// TODO: Show login view first
//
//			// For now, just show a placeholder window
//			JFrame frame = new JFrame("AI Study Companion");
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			frame.setSize(800, 600);
//
//			JLabel placeholder = new JLabel("Welcome to AI Study Companion", SwingConstants.CENTER);
//			placeholder.setFont(placeholder.getFont().deriveFont(24f));
//			frame.add(placeholder);
//
//			frame.setLocationRelativeTo(null);
//			frame.setVisible(true);

            // Now actual code
            AppBuilder appBuilder = new AppBuilder();
            JFrame app = appBuilder
                    .addDashboardView()
                    .addSettingsView()
//                    .addChooseStudySessionView()
//                    .addUploadSessionMaterialsView()
                    .addStudySessionEndView()
                    .addVariableSessionView()
                    .addStudyQuizView()
                    .addFileManagerView()
                    .addQuizHistoryView()
                    .addScheduleSessionView()
                    .addStudyMetricsView()
                    .addStudySessionConfigView()
                    .addConfigStudySessionUseCase()
                    .build();

            app.pack();
            app.setLocationRelativeTo(null);
            app.setVisible(true);
		});
	}
}
