package view;

import app.AppBuilder;

import javax.swing.*;
import java.awt.*;

/**
 * View for uploading and managing reference materials.
 */
public class UploadMaterialsView extends View {
    public UploadMaterialsView() {
        super("uploadMaterials");
        JPanel header = new ViewHeader("Upload Materials");

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        JPanel promptPanel = new JPanel();

        JLabel promptLabel = new JLabel("Upload and manage your reference materials here.");
        JTextArea promptArea = new JTextArea(3, 30);
        promptArea.setLineWrap(true);

        promptPanel.add(promptLabel);
        promptPanel.add(new JScrollPane(promptArea));

        JButton uploadButton = new JButton("Upload File");

        JPanel uploadButtonPanel = new JPanel();
        uploadButtonPanel.add(uploadButton);

        JPanel deletePanel = new JPanel();

        JLabel materialsLabel = new JLabel("Uploaded Materials:");
        JList<String> materialsList = new JList<>(new DefaultListModel<>());
        materialsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        deletePanel.add(materialsLabel);
        deletePanel.add(new JScrollPane(materialsList));

        JButton deleteButton = new JButton("Delete Selected");

        deletePanel.add(deleteButton);

        main.add(promptPanel);
        main.add(uploadButtonPanel);
        main.add(deletePanel);

        JPanel dashboard = new JPanel();

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            // Navigate back to dashboard
            AppBuilder.viewManagerModel.setView("dashboard");
        });

        dashboard.add(cancelButton);

        this.add(header, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
        this.add(dashboard, BorderLayout.SOUTH);
    }

//	private JButton uploadButton;
//	private JButton deleteButton;
//	private JList<String> materialsList;
//	private JTextArea promptArea;
//	private DefaultListModel<String> listModel;
//
//	public UploadMaterialsView() {
//		initializeComponents();
//		layoutComponents();
//	}
//
//	private void initializeComponents() {
//		uploadButton = new JButton("Upload File");
//		deleteButton = new JButton("Delete Selected");
//
//		listModel = new DefaultListModel<>();
//		materialsList = new JList<>(listModel);
//		materialsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//
//		promptArea = new JTextArea(3, 30);
//		promptArea.setLineWrap(true);
//	}
//
//	private void layoutComponents() {
//		setLayout(new BorderLayout());
//
//		// TODO: Properly layout the upload materials components
//		// Top: Upload button and prompt
//		// Middle: List of uploaded materials
//		// Bottom: Delete button
//
//		JPanel topPanel = new JPanel();
//		topPanel.add(new JLabel("Description:"));
//		topPanel.add(new JScrollPane(promptArea));
//		topPanel.add(uploadButton);
//
//		add(topPanel, BorderLayout.NORTH);
//		add(new JScrollPane(materialsList), BorderLayout.CENTER);
//		add(deleteButton, BorderLayout.SOUTH);
//	}
//
//	public void addMaterial(String materialName) {
//		listModel.addElement(materialName);
//	}
//
//	public void removeMaterial(String materialName) {
//		listModel.removeElement(materialName);
//	}
//
//	public String[] getSelectedMaterials() {
//		return materialsList.getSelectedValuesList().toArray(new String[0]);
//	}
//
//	public String getPrompt() {
//		return promptArea.getText();
//	}
//
//	public void setUploadButtonListener(java.awt.event.ActionListener listener) {
//		uploadButton.addActionListener(listener);
//	}
//
//	public void setDeleteButtonListener(java.awt.event.ActionListener listener) {
//		deleteButton.addActionListener(listener);
//	}
}
