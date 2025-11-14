package view;

import app.AppBuilder;

import javax.swing.*;
import java.awt.*;

public class FileManagerView extends View {
    public FileManagerView() {
        super("fileManager");

        JPanel header = new ViewHeader("Manage Files");
        JPanel main = new JPanel();

        JButton returnButton = new JButton("Return Home");
        returnButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("dashboard");
        });
        main.add(returnButton);
        this.add(header, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
    }
}
