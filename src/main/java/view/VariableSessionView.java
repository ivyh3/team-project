package view;

import app.AppBuilder;

import javax.swing.*;
import java.awt.*;

public class VariableSessionView extends View {
    public VariableSessionView() {
        super("variableSession");

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        JLabel timeStudiedHeader = new JLabel("Session Duration:");
        timeStudiedHeader.setFont(new Font(null, Font.BOLD, 52));
        timeStudiedHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel durationLabel = new JLabel("2h 45m 02s");
        durationLabel.setFont(new Font(null, Font.BOLD, 52));
        durationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton finalizeSession = new JButton("Finalize Session");
        finalizeSession.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("studySessionEnd");
        });
        finalizeSession.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(Box.createVerticalGlue());
        main.add(timeStudiedHeader);
        main.add(durationLabel);
        main.add(Box.createVerticalGlue());
        main.add(finalizeSession);
        main.add(Box.createVerticalGlue());
        this.add(main, BorderLayout.CENTER);
    }
}
