package view;

import interface_adapter.controller.UploadReferenceMaterialController;
import interface_adapter.view_model.UploadMaterialsViewModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * UI for uploading and managing reference materials.
 */
public class UploadMaterialsView extends View {

    private final JButton uploadButton;
    private final JButton backButton;
    private final DefaultListModel<String> fileListModel;
    private final JList<String> fileList;
    private final UploadMaterialsViewModel viewModel;
    private final File storageDir = new File("uploaded_materials");

    public UploadMaterialsView(UploadMaterialsViewModel viewModel) {
        super("uploadMaterials");
        this.viewModel = viewModel;

        if (!storageDir.exists()) storageDir.mkdirs();

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 400));

        fileListModel = new DefaultListModel<>();
        fileList = new JList<>(fileListModel);
        JScrollPane scrollPane = new JScrollPane(fileList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Uploaded Files"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        uploadButton = new JButton("Upload");
        backButton = new JButton("Back");
        buttonPanel.add(uploadButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        viewModel.addObserver(files -> {
            fileListModel.clear();
            files.forEach(fileListModel::addElement);
        });

        loadExistingFiles();
    }

    public File[] showFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) return chooser.getSelectedFiles();
        return new File[0];
    }

    public void setUploadController(UploadReferenceMaterialController controller, String userId) {
        uploadButton.addActionListener(e -> {
            File[] selectedFiles = showFileChooser();
            for (File file : selectedFiles) {
                try {
                    File destFile = new File(storageDir, file.getName());
                    java.nio.file.Files.copy(file.toPath(), destFile.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                    controller.uploadReferenceMaterial(userId, destFile, null);
                    viewModel.addMaterial(destFile.getName());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Failed to upload: " + file.getName());
                    ex.printStackTrace();
                }
            }
        });
    }

    public void setBackButtonListener(java.awt.event.ActionListener listener) {
        backButton.addActionListener(listener);
    }

    private void loadExistingFiles() {
        File[] files = storageDir.listFiles();
        if (files != null) {
            for (File file : files) viewModel.addMaterial(file.getName());
        }
    }
}