package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import interface_adapter.controller.EndStudySessionController;
import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionState;
import interface_adapter.view_model.StudySessionViewModel;

/**
 * View for a study session. Depending on the session type in provided state, will display a
 * countdown timer (timed session) or a stopwatch (fixed session).
 * On session termination, whether through time running out or manual intervention, will send the
 * user to the StudySessionEndView and display their study duration.
 */
public class StudySessionView extends StatefulView<StudySessionState> {
    public static final int TEXT_HUGE = 52;
    public static final int MIN_PER_HOUR = 60;
    public static final int SEC_PER_MIN = 60;
    private static final int ONE_SECOND = 1000;
    private static final Map<StudySessionConfigState.SessionType, String> HEADER_LABELS = Map.of(
        StudySessionConfigState.SessionType.FIXED, "Time left:",
        StudySessionConfigState.SessionType.VARIABLE, "Time studied:"
    );
    private final JLabel durationLabel = new JLabel();
    private final JLabel headerLabel = new JLabel();
    private final Timer uiTimer;
    private EndStudySessionController endStudySessionController;
    private DashboardViewModel dashboardViewModel;

    public StudySessionView(StudySessionViewModel studySessionViewModel, DashboardViewModel dashboardViewModel) {
        super("studySession", studySessionViewModel);
        this.dashboardViewModel = dashboardViewModel;

        final JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        headerLabel.setText("Loading...");
        headerLabel.setFont(new Font(null, Font.BOLD, TEXT_HUGE));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        durationLabel.setText("Loading...");
        durationLabel.setFont(new Font(null, Font.BOLD, TEXT_HUGE));
        durationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        uiTimer = new Timer(ONE_SECOND, event -> {
            // Every second, update the timer label. Supposedly OK based on what prof said.
            updateDurationLabel();
            // For a fixed session, once time is out, automatically trigger the end session use case interaction.
            if (viewModel.getState().getSessionType() == StudySessionConfigState.SessionType.FIXED
                && viewModel.getState().getRemainingDuration().isZero()) {
                final StudySessionState state = viewModel.getState();
                endStudySessionController.execute(
                    this.dashboardViewModel.getState().getUserId(), state);
            }
        });

        final JButton finalizeSession = new JButton("Finalize Session");
        finalizeSession.addActionListener(event -> {
            final StudySessionState state = viewModel.getState();
            endStudySessionController.execute(
                this.dashboardViewModel.getState().getUserId(), state);
        });
        finalizeSession.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(Box.createVerticalGlue());
        main.add(headerLabel);
        main.add(durationLabel);
        main.add(Box.createVerticalGlue());
        main.add(finalizeSession);
        main.add(Box.createVerticalGlue());
        this.add(main, BorderLayout.CENTER);
    }

    /**
     * Returns a formatted string in HH:MM:SS form based on the given Duration.
     *
     * @param duration The duration to format.
     * @return String representation of the duration as HH:MM:SS
     */
    private String formatDuration(Duration duration) {
        final long hours = duration.toHours();
        final long minutes = duration.toMinutes() % MIN_PER_HOUR;
        final long seconds = duration.getSeconds() % SEC_PER_MIN;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Updates the label for the timer/stopwatch depending on the type of the session.
     */
    private void updateDurationLabel() {
        if (Objects.requireNonNull(viewModel.getState().getSessionType())
            == StudySessionConfigState.SessionType.FIXED) {
            durationLabel.setText(formatDuration(viewModel.getState().getRemainingDuration()));
        }
        else if (viewModel.getState().getSessionType() == StudySessionConfigState.SessionType.VARIABLE) {
            durationLabel.setText(formatDuration(viewModel.getState().getDurationElapsed()));
        }
    }

    /**
     * Sets the controller for ending the study session.
     *
     * @param controller The controller to use.
     */
    public void addEndStudySessionController(EndStudySessionController controller) {
        this.endStudySessionController = controller;
    }

    /**
     * Initializes the various UI components for this study session, and starts the timer
     * to update the duration label every second.
     */
    private void onSessionStart() {
        uiTimer.start();
        headerLabel.setText(HEADER_LABELS.get(viewModel.getState().getSessionType()));
        updateDurationLabel();
    }

    /**
     * Stops the timer which updates the duration label.
     */
    private void onSessionEnd() {
        uiTimer.stop();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final StudySessionState newState = (StudySessionState) evt.getNewValue();

        if (newState.isActive()) {
            // Active session (i.e., started session)
            onSessionStart();
        }
        else {
            // The session is over, so perform cleanup (stop timer)
            onSessionEnd();
        }
    }
}
