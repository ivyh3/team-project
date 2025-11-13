package view;

import app.AppBuilder;

import javax.swing.*;
import java.awt.*;

public class UploadSessionMaterialsView extends View{
    public UploadSessionMaterialsView() {
        super("uploadSessionMaterials");
        JPanel header = new ViewHeader("Study Materials");

        JPanel main = new JPanel();

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("dashboard");
        });

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("variableSession");
        });
        main.add(cancelButton);
        main.add(nextButton);

        this.add(header, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);

    }

}
