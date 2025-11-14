package view;

import app.AppBuilder;

import javax.swing.*;
import java.awt.*;

public class StudySessionEndView extends View {
    public StudySessionEndView() {
        super("studySessionEnd");

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        JLabel studySessionEndLabel = new JLabel("Study Session End");
        studySessionEndLabel.setFont(new Font(null, Font.BOLD, 52));
        studySessionEndLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel durationLabel = new JLabel("You studied for 1h 40m");
        durationLabel.setFont(new Font(null, Font.BOLD, 28));
        durationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton quizMeButton = new JButton("Quiz Me");
        quizMeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        quizMeButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("studyQuiz");
        });

        main.add(Box.createVerticalGlue());
        main.add(studySessionEndLabel);
        main.add(durationLabel);
        main.add(Box.createVerticalGlue());
        main.add(quizMeButton);
        main.add(Box.createVerticalGlue());

        this.add(main, BorderLayout.CENTER);
    }
}
