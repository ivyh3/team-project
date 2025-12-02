package view;

import interface_adapter.controller.UploadReferenceMaterialController;
import interface_adapter.view_model.UploadMaterialsState;
import interface_adapter.view_model.UploadMaterialsViewModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class UploadMaterialsView extends StatefulView<UploadMaterialsState> {

    private UploadReferenceMaterialController controller;    // 🔥 added
    private final JButton uploadButton;
    private final JButton backButton;
    private final DefaultListModel<String> fileListModel;
    private final JList<String> fileList;
    private final UploadMaterialsViewModel viewModel;
    private final File storageDir = new File("uploaded_materials");

    public UploadMaterialsView(UploadMaterialsViewModel viewModel) {
        super("uploadMaterials");  // ❗ constructor unchanged
        this.viewModel = viewModel;

        if (!storageDir.exists()) storageDir.mkdirs();

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 400));

        fileListModel = new DefaultListModel<>();
        fileList = new JList<>(fileListModel);
        JScrollPane scrollPane = new JScrollPane(fileList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Uploaded Files"));
        add(scrollPane, BorderLayout.CENTER);

        // ---- Buttons ----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        uploadButton = new JButton("Upload");
        uploadButton.addActionListener(e -> selectPdfAndUpload());

        backButton = new JButton("Back");

        buttonPanel.add(uploadButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // update UI when ViewModel changes
        viewModel.addObserver(files -> {
            fileListModel.clear();
            files.forEach(fileListModel::addElement);
        });

        loadExistingFiles();
    }

    /** Called from AppBuilder — required to avoid 'cannot resolve symbol controller' */
    public void setUploadController(UploadReferenceMaterialController controller) {
        this.controller = controller;
    }

    /** Handles selecting a PDF and saving it locally */
    private void selectPdfAndUpload() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));

        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File selected = chooser.getSelectedFile();
        File dest = new File(storageDir, selected.getName());

        try {
            Files.copy(selected.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            viewModel.addMaterial(dest.getName());   // update view

            if (controller != null) controller.uploadPdf(dest); // call backend only if injected
            JOptionPane.showMessageDialog(this, "Upload successful.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Upload failed: " + ex.getMessage());
        }
    }

    /** Loads existing folder contents on startup */
    private void loadExistingFiles() {
        File[] files = storageDir.listFiles();
        if (files != null)
            for (File file : files)
                viewModel.addMaterial(file.getName());
    }
}