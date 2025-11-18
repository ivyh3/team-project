package view;

import app.AppBuilder;

import javax.swing.*;
import java.awt.*;

public class QuizHistoryView extends View {
    public QuizHistoryView() {
        super("quizHistory");

        JPanel header = new ViewHeader("Past Quizzes");
        JPanel main = new JPanel();
        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("dashboard");
        });
        JButton reviewQuizButton = new JButton("Review Quiz");
        reviewQuizButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("quizReview");
        });

        main.add(returnButton);
        main.add(reviewQuizButton);


        this.add(header, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
    }
}