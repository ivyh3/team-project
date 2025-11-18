package view;

import app.AppBuilder;
import interface_adapter.view_model.StudySessionEndState;
import interface_adapter.view_model.StudySessionEndViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.time.Duration;

public class StudySessionEndView extends StatefulView<StudySessionEndState> {
    private final JLabel resultLabel = new JLabel();

    public StudySessionEndView(StudySessionEndViewModel viewModel) {
        super("studySessionEnd", viewModel);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        JLabel studySessionEndLabel = new JLabel("Study Session End");
        studySessionEndLabel.setFont(new Font(null, Font.BOLD, 52));
        studySessionEndLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setFont(new Font(null, Font.BOLD, 28));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton quizMeButton = new JButton("Quiz Me");
        quizMeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        quizMeButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("studyQuiz");
        });

        main.add(Box.createVerticalGlue());
        main.add(studySessionEndLabel);
        main.add(resultLabel);
        main.add(Box.createVerticalGlue());
        main.add(quizMeButton);
        main.add(Box.createVerticalGlue());

        this.add(main, BorderLayout.CENTER);
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        StudySessionEndState state = (StudySessionEndState) evt.getNewValue();
        System.out.println(state);
        resultLabel.setText("You studied for " + formatDuration(state.getDuration()));
    }
}