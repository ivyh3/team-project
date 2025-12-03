package view;

import app.AppBuilder;
import interface_adapter.controller.UploadReferenceMaterialController;
import interface_adapter.view_model.UploadMaterialsViewModel;
import interface_adapter.view_model.UploadMaterialsState;
import interface_adapter.view_model.ViewModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.List;

public class UploadMaterialsView extends StatefulView<UploadMaterialsState> {

    private final String viewName = "uploadMaterials";
    private final ViewModel<UploadMaterialsState> viewModel;
    private final UploadMaterialsViewModel uploadMaterialsViewModel;

    private JButton uploadButton;
    private JButton returnButton;
    private JButton deleteButton;
    private JLabel statusLabel;
    private JList<String> materialsList;
    private DefaultListModel<String> listModel;

    private final UploadReferenceMaterialController uploadController;

    public UploadMaterialsView(UploadMaterialsViewModel uploadMaterialsViewModel,
                               UploadReferenceMaterialController controller) {
        super("uploadMaterials", uploadMaterialsViewModel.getViewModel());
        this.uploadMaterialsViewModel = uploadMaterialsViewModel;
        this.viewModel = uploadMaterialsViewModel.getViewModel();
        this.uploadController = controller;

        setupUI();
        setupActions();
        refreshMaterialList();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));

        uploadButton = new JButton("Upload PDF");
        deleteButton = new JButton("Delete Selected");
        returnButton = new JButton("Return");
        statusLabel = new JLabel("Select a PDF to upload.", SwingConstants.CENTER);

        listModel = new DefaultListModel<>();
        materialsList = new JList<>(listModel);
        materialsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(materialsList);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(uploadButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(returnButton);

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);
    }

    private void setupActions() {
        uploadButton.addActionListener(e -> selectPdfAndUpload());
        deleteButton.addActionListener(e -> deleteSelectedMaterials());
        returnButton.addActionListener(e -> AppBuilder.viewManagerModel.setView("settings"));
    }

    private void selectPdfAndUpload() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File selectedFile = fileChooser.getSelectedFile();

        try {
            // Always fetch logged-in user ID from ViewModel or session
            String userId = uploadMaterialsViewModel.getCurrentUserId();
            if (userId == null || userId.isBlank()) {
                throw new IllegalStateException("User must be logged in to upload files.");
            }

            uploadController.uploadReferenceMaterial(userId, selectedFile, null);

            // Update the view state
            viewModel.getState().addMaterial(selectedFile.getName());
            viewModel.firePropertyChange(); // notify observers

            // Refresh list immediately
            refreshMaterialList();

            statusLabel.setText("Upload successful: " + selectedFile.getName());
            JOptionPane.showMessageDialog(this,
                    "File uploaded successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            statusLabel.setText("Upload failed: " + (ex.getMessage() != null ? ex.getMessage() : "Unknown error"));
            JOptionPane.showMessageDialog(this,
                    "Failed to upload file: " + (ex.getMessage() != null ? ex.getMessage() : "Unknown error"),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedMaterials() {
        List<String> selectedFiles = materialsList.getSelectedValuesList();

        if (selectedFiles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No files selected for deletion.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the selected materials?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            for (String fileName : selectedFiles) {
                viewModel.getState().removeMaterial(fileName);
            }
            viewModel.firePropertyChange();
            refreshMaterialList();
            statusLabel.setText("Selected materials deleted.");
        }
    }

    private void refreshMaterialList() {
        listModel.clear();
        for (String material : viewModel.getState().getUploadedMaterials()) {
            listModel.addElement(material);
        }
    }

    public String getViewName() {
        return viewName;
    }
}