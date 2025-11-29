package view;

import app.AppBuilder;
import interface_adapter.controller.UploadReferenceMaterialController;
import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.UploadMaterialsViewModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class UploadMaterialsView extends View {

    private final JButton uploadButton;
    private final JButton deleteButton;
    private final JButton cancelButton;
    private final JList<String> materialsList;
    private final DefaultListModel<String> listModel;
    private UploadReferenceMaterialController uploadController;
    private final DashboardViewModel dashboardViewModel;
    private final UploadMaterialsViewModel uploadViewModel;

    public UploadMaterialsView(DashboardViewModel dashboardViewModel, UploadMaterialsViewModel uploadViewModel) {
        super("uploadMaterials");
        this.dashboardViewModel = dashboardViewModel;
        this.uploadViewModel = uploadViewModel;

        JPanel header = new ViewHeader("Upload Materials");

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
        main.add(uploadButtonPanel);
        main.add(deletePanel);

        // Dashboard/cancel panel
        cancelButton = new JButton("Cancel");
        JPanel dashboard = new JPanel();
        dashboard.add(cancelButton);

        this.add(header, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
        this.add(dashboard, BorderLayout.SOUTH);

        // Observe ViewModel
        uploadViewModel.addObserver(state -> SwingUtilities.invokeLater(() -> {
            listModel.clear();
            state.getUploadedMaterials().forEach(listModel::addElement);
        }));
    }

    // ------------- File chooser method -------------
    public File openPdfChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select PDF to upload");
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    // --- Controller binding ---
    public void setUploadController(UploadReferenceMaterialController controller) {
        this.uploadController = controller;

        uploadButton.addActionListener(e -> {
            File selectedPdf = openPdfChooser();
            if (selectedPdf == null) return;

            String userId = dashboardViewModel.getState().getUserId();
            try {
                uploadController.uploadReferenceMaterial(userId, selectedPdf);
                // Update ViewModel so view refreshes
                uploadViewModel.addMaterial(selectedPdf.getName());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to upload file: " + ex.getMessage());
            }
        });

        deleteButton.addActionListener(e -> {
            String[] selected = materialsList.getSelectedValuesList().toArray(new String[0]);
            if (selected.length == 0) {
                JOptionPane.showMessageDialog(this, "Select a file to delete.");
                return;
            }
            String userId = dashboardViewModel.getState().getUserId();
            for (String fileName : selected) {
                try {
                    uploadController.deleteReferenceMaterial(userId, fileName);
                    uploadViewModel.removeMaterial(fileName);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to delete file: " + ex.getMessage());
                }
            }
        });

        cancelButton.addActionListener(e -> AppBuilder.viewManagerModel.setView("dashboard"));
    }
}
