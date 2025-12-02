package view;

import app.AppBuilder;

import javax.swing.*;
import java.awt.*;

/**
 * View for uploading and managing reference materials.
 */
public class UploadMaterialsView extends View {
    private final JButton uploadButton;
    private final JButton deleteButton;
    private final JList<String> materialsList;
    private final JTextArea promptArea;
    private final DefaultListModel<String> listModel;

    public UploadMaterialsView() {
        super("uploadMaterials");
        final JPanel header = new ViewHeader("Upload Materials");

        final JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        final JPanel promptPanel = new JPanel();

        final JLabel promptLabel = new JLabel("Upload and manage your reference materials here.");
        promptArea = new JTextArea(3, 30);
        promptArea.setLineWrap(true);

        promptPanel.add(promptLabel);
        promptPanel.add(new JScrollPane(promptArea));

        uploadButton = new JButton("Upload File");

        final JPanel uploadButtonPanel = new JPanel();
        uploadButtonPanel.add(uploadButton);

        final JPanel deletePanel = new JPanel();

        final JLabel materialsLabel = new JLabel("Uploaded Materials:");
        listModel = new DefaultListModel<>();
        materialsList = new JList<>(listModel);
        materialsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        deletePanel.add(materialsLabel);
        deletePanel.add(new JScrollPane(materialsList));

        deleteButton = new JButton("Delete Selected");

        deletePanel.add(deleteButton);

        main.add(promptPanel);
        main.add(uploadButtonPanel);
        main.add(deletePanel);

        final JPanel dashboard = new JPanel();

        final JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            // Navigate back to dashboard
            AppBuilder.viewManagerModel.setView("dashboard");
        });

        dashboard.add(cancelButton);

        this.add(header, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
        this.add(dashboard, BorderLayout.SOUTH);
    }

    public void addMaterial(String materialName) {
        listModel.addElement(materialName);
    }

    public void removeMaterial(String materialName) {
        listModel.removeElement(materialName);
    }

    public String[] getSelectedMaterials() {
        return materialsList.getSelectedValuesList().toArray(new String[0]);
    }

    public String getPrompt() {
        return promptArea.getText();
    }

    public void setUploadButtonListener(java.awt.event.ActionListener listener) {
        uploadButton.addActionListener(listener);
    }

    public void setDeleteButtonListener(java.awt.event.ActionListener listener) {
        deleteButton.addActionListener(listener);
    }
}
