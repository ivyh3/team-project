package view;

import java.awt.*;

import javax.swing.*;

import app.AppBuilder;

public class UploadSessionMaterialsView extends View {
    public UploadSessionMaterialsView() {
        super("uploadSessionMaterials");
        final JPanel header = new ViewHeader("Study Materials");

        final JPanel main = new JPanel();

        final JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("dashboard");
        });

        final JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("variableSession");
        });
        main.add(cancelButton);
        main.add(nextButton);

        this.add(header, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);

    }

}
