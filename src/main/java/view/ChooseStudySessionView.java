package view;



import app.AppBuilder;

import javax.swing.*;
import java.awt.*;

public class ChooseStudySessionView extends View {

    public ChooseStudySessionView() {
        super("chooseStudySession");

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        JPanel header = new ViewHeader("Start Study Session");


        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));

        JPanel timedSessionContainer = new JPanel();
        timedSessionContainer.setLayout(new BoxLayout(timedSessionContainer, BoxLayout.Y_AXIS));
        JPanel variableSessionContainer = new JPanel();
        variableSessionContainer.setLayout(new BoxLayout(variableSessionContainer, BoxLayout.Y_AXIS));

        buttonContainer.add(timedSessionContainer);
        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(variableSessionContainer);

        JLabel timedSessionHeading = new JLabel("Timed Session");
        timedSessionHeading.setFont(new Font(null, Font.BOLD, 28));
        JLabel timedSessionLabel = new JLabel("Allocate a specified focus time for studying.");
        timedSessionLabel.setFont(new Font(null, Font.ITALIC, 16));
        JButton timedSessionButton = new JButton("I want a timed session!");
        timedSessionButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("setSessionDuration");
        });

        timedSessionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        timedSessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timedSessionHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        timedSessionContainer.add(timedSessionHeading);
        timedSessionContainer.add(timedSessionLabel);
        timedSessionContainer.add(Box.createVerticalGlue());
        timedSessionContainer.add(timedSessionButton);

        JLabel variableSessionHeading = new JLabel("Variable Session");
        variableSessionHeading.setFont(new Font(null, Font.BOLD, 28));
        JLabel variableSessionLabel = new JLabel("Study until you're ready.");
        variableSessionLabel.setFont(new Font(null, Font.ITALIC, 16));

        JButton variableSessionButton = new JButton("I want a variable session!");
        variableSessionButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("uploadSessionMaterials");
        });

        variableSessionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        variableSessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        variableSessionHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        variableSessionContainer.add(variableSessionHeading);
        variableSessionContainer.add(variableSessionLabel);
        variableSessionContainer.add(Box.createVerticalGlue());
        variableSessionContainer.add(variableSessionButton);


        final JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> {
            if (e.getSource().equals(returnButton)) {
                AppBuilder.viewManagerModel.setView("dashboard");
            }
        });
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(buttonContainer);
        main.add(Box.createVerticalGlue());
        main.add(returnButton);

        this.add(header, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
    }



}
