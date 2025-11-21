package view;

import interface_adapter.controller.EndStudySessionController;
import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionState;
import interface_adapter.view_model.StudySessionViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.time.Duration;
import java.util.Map;

/**
 * View for a study session. Depending on the session type in provided state, will display a
 * countdown timer (timed session) or a stopwatch (fixed session).
 * <p>
 * On session termination, whether through time running out or manual intervention, will send the
 * user to the StudySessionEndView and display their study duration.
 */
public class StudySessionView extends StatefulView<StudySessionState> {
    private static final int ONE_SECOND = 1000;
    private static final Map<StudySessionConfigState.SessionType, String> HEADER_LABELS = Map.of(
            StudySessionConfigState.SessionType.FIXED, "Time left:",
            StudySessionConfigState.SessionType.VARIABLE, "Time studied:"
    );
    private final JLabel durationLabel = new JLabel();
    private final JLabel headerLabel = new JLabel();
    private final Timer uiTimer;
    private EndStudySessionController endStudySessionController;

    public StudySessionView(StudySessionViewModel studySessionViewModel) {
        super("studySession", studySessionViewModel);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        headerLabel.setText("Loading...");
        headerLabel.setFont(new Font(null, Font.BOLD, 52));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        durationLabel.setText("Loading...");
        durationLabel.setFont(new Font(null, Font.BOLD, 52));
        durationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        uiTimer = new Timer(ONE_SECOND, e -> {
            // Every second, update the timer label.
            System.out.println("Timer update");
            updateDurationLabel();

            // Todo: probably have this logic set in the viewmodel? or controller? I don't know since time is involved
            // For a fixed session, once time is out, automatically trigger the end session use case interaction.
            if (viewModel.getState().getSessionType() == StudySessionConfigState.SessionType.FIXED &&
                    viewModel.getState().getRemainingDuration().isZero()) {

                StudySessionState state = viewModel.getState();
                endStudySessionController.execute(state);
            }
        });

        JButton finalizeSession = new JButton("Finalize Session");
        finalizeSession.addActionListener(e -> {
            StudySessionState state = viewModel.getState();
            endStudySessionController.execute(state);
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
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Updates the label for the timer/stopwatch depending on the type of the session.
     */
    private void updateDurationLabel() {
        switch (viewModel.getState().getSessionType()) {
            case FIXED:
                durationLabel.setText(formatDuration(viewModel.getState().getRemainingDuration()));
                break;
            case VARIABLE:
                durationLabel.setText(formatDuration(viewModel.getState().getDurationElapsed()));
        }
    }

    /**
     * Sets the controller for ending the study session.
     *
     * @param endStudySessionController The controller to use.
     */
    public void addEndStudySessionController(EndStudySessionController endStudySessionController) {
        this.endStudySessionController = endStudySessionController;
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
        System.out.println(newState);

        // Only captures a property change on setting the study session state at the start,
        // as timer logic is entirely UI concern, not affecting any state. It's implicitly updated
        // through the passing of time.

        if (newState.isActive()) {
            // Initialize this view for the study session
            onSessionStart();
        } else {
            // The session is over, so perform cleanup
            onSessionEnd();
        }
    }
}
