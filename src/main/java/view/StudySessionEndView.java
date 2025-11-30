package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.time.Duration;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import app.AppBuilder;
import interface_adapter.view_model.StudySessionEndState;
import interface_adapter.view_model.StudySessionEndViewModel;

/**
 * The view to display the summary (duration elapsed) for a study session.
 */
public class StudySessionEndView extends StatefulView<StudySessionEndState> {
    public static final int MIN_PER_HOUR = 60;
    public static final int SEC_PER_MIN = 60;
    public static final int TEXT_HUGE = 52;
    public static final int TEXT_XL = 28;
    private final JLabel resultLabel = new JLabel();

    public StudySessionEndView(StudySessionEndViewModel viewModel) {
        super("studySessionEnd", viewModel);

        final JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        final JLabel studySessionEndLabel = new JLabel("Study Session End");
        studySessionEndLabel.setFont(new Font(null, Font.BOLD, TEXT_HUGE));
        studySessionEndLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        resultLabel.setFont(new Font(null, Font.BOLD, TEXT_XL));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JButton quizMeButton = new JButton("Quiz Me");
        quizMeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quizMeButton.addActionListener(event -> {
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

    /**
     * Return a String representation of a given duration in HHh MMm SSs format.
     *
     * @param duration The duration to format.
     * @return The formatted duration in HHh MMm SSs format.
     */
    private String formatDuration(Duration duration) {
        final long hours = duration.toHours();
        final long minutes = duration.toMinutes() % MIN_PER_HOUR;
        final long seconds = duration.getSeconds() % SEC_PER_MIN;
        return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final StudySessionEndState state = (StudySessionEndState) evt.getNewValue();
        resultLabel.setText("You studied for " + formatDuration(state.getDuration()));
    }
}
