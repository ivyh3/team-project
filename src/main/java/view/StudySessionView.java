package view;

import app.AppBuilder;
import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionState;
import interface_adapter.view_model.StudySessionViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.time.Duration;
import java.util.Map;


public class StudySessionView extends StatefulView<StudySessionState> {
    // Todo: Seperate these into two different views? And then have different types of state???

    private final JLabel durationLabel = new JLabel();
    private final JLabel headerLabel = new JLabel();
    private Map<StudySessionConfigState.SessionType, String> HEADER_LABEL = Map.of(
            StudySessionConfigState.SessionType.FIXED, "Time left:",
            StudySessionConfigState.SessionType.VARIABLE, "Time studied:"
    );
    private static int ONE_SECOND = 1000;
    private Timer uiTimer;

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
            System.out.println("Timer update");
            updateDurationLabel();
        });

        JButton finalizeSession = new JButton("Finalize Session");
        finalizeSession.addActionListener(e -> {
            // TODO: create a controller.
            AppBuilder.viewManagerModel.setView("studySessionEnd");
            viewModel.getState().setActive(false);
            viewModel.firePropertyChange();
            // Use case interactor will need to create an actual study session entity and save it
            // using the end time (the time when this button was pressed
            // Then hand off reference materials to create a quiz?????????
            // Is it just me or is my use case cooked bro time shenanigans + config (many use cases in one ???) + multiple views

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

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void updateDurationLabel() {
        switch (viewModel.getState().getSessionType()) {
            case FIXED:
                System.out.println(viewModel.getState().getRemainingDuration());
                durationLabel.setText(formatDuration(viewModel.getState().getRemainingDuration()));
                break;
            case VARIABLE:
                durationLabel.setText(formatDuration(viewModel.getState().getDurationElapsed()));
        }
    }

    public void onSessionStart() {
        uiTimer.start();
        headerLabel.setText(HEADER_LABEL.get(viewModel.getState().getSessionType()));
        updateDurationLabel();
    }

    public void onSessionEnd() {
        uiTimer.stop();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (viewModel.getState().isActive()) {
            onSessionStart();
        }
        else {
            onSessionEnd();
        }
    }
}
