package view;

import javax.swing.*;
import java.awt.*;

public class UploadMaterialsView extends View {

    private final JButton uploadButton;
    private final JButton deleteButton;
    private final JButton cancelButton;
    private final JList<String> materialsList;
    private final JTextArea promptArea;
    private final DefaultListModel<String> listModel;

    public UploadMaterialsView() {
        super("uploadMaterials");

        JPanel header = new ViewHeader("Upload Materials");

        // Prompt panel
        JPanel promptPanel = new JPanel();
        JLabel promptLabel = new JLabel("Upload and manage your reference materials here.");
        promptArea = new JTextArea(3, 30);
        promptArea.setLineWrap(true);
        promptPanel.add(promptLabel);
        promptPanel.add(new JScrollPane(promptArea));

        // Upload button panel
        uploadButton = new JButton("Upload File");
        JPanel uploadButtonPanel = new JPanel();
        uploadButtonPanel.add(uploadButton);

        // Materials list panel
        listModel = new DefaultListModel<>();
        materialsList = new JList<>(listModel);
        materialsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        deleteButton = new JButton("Delete Selected");
        JPanel deletePanel = new JPanel(new BorderLayout());
        deletePanel.add(new JLabel("Uploaded Materials:"), BorderLayout.NORTH);
        deletePanel.add(new JScrollPane(materialsList), BorderLayout.CENTER);
        deletePanel.add(deleteButton, BorderLayout.SOUTH);

        // Main panel
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.add(promptPanel);
        main.add(uploadButtonPanel);
        main.add(deletePanel);

        // Dashboard/cancel panel
        cancelButton = new JButton("Cancel");
        JPanel dashboard = new JPanel();
        dashboard.add(cancelButton);

        this.add(header, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
        this.add(dashboard, BorderLayout.SOUTH);
    }

    // --- Public API for controller ---

    public void addMaterial(String materialName) {
        SwingUtilities.invokeLater(() -> listModel.addElement(materialName));
    }

    public void removeMaterial(String materialName) {
        SwingUtilities.invokeLater(() -> listModel.removeElement(materialName));
    }

    public String[] getSelectedMaterials() {
        return materialsList.getSelectedValuesList().toArray(new String[0]);
    }

    public String getPrompt() {
        return promptArea.getText();
    }

    public void bindUploadAction(Runnable action) {
        uploadButton.addActionListener(e -> action.run());
    }

    public void bindDeleteAction(Runnable action) {
        deleteButton.addActionListener(e -> action.run());
    }

    public void bindCancelAction(Runnable action) {
        cancelButton.addActionListener(e -> action.run());
    }
}
