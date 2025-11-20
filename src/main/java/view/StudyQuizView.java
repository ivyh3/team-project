package view;

import app.AppBuilder;

import javax.swing.*;
import java.awt.*;

public class StudyQuizView extends View{
    public StudyQuizView() {
        super("studyQuiz");

        JPanel header = new ViewHeader("Quiz");
        header.add(new JLabel("1/10"), BorderLayout.EAST);
        JPanel main = new JPanel();

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("dashboard");
        });

        main.add(nextButton);

        this.add(header, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
    }
}
