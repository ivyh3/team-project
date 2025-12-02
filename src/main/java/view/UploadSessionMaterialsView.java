package view;

import interface_adapter.view_model.UploadMaterialsState;
import interface_adapter.view_model.ViewModel;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class UploadSessionMaterialsView extends StatefulView<UploadMaterialsState> {

    private JPanel mainPanel = new JPanel();
    private JButton uploadButton = new JButton("Upload PDF");
    private JButton cancelButton = new JButton("Cancel");
    private JLabel title = new JLabel("Upload Session Materials", SwingConstants.CENTER);

    public UploadSessionMaterialsView(String viewName, ViewModel uploadMaterialsViewModel) {
        super(viewName, uploadMaterialsViewModel);

        mainPanel.setLayout(new BorderLayout());
        title.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(uploadButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // 🔥 THIS MAKES THE FILE CHOOSER WORK
        uploadButton.addActionListener(e -> openPdfChooser());

        cancelButton.addActionListener(e -> {
            // Switch back to previous screen if needed
            System.out.println("Cancel pressed");
        });
    }

    private void openPdfChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

        int result = chooser.showOpenDialog(mainPanel); // ❗ pass a Swing Component
        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            System.out.println("Selected File: " + selected.getAbsolutePath());

            // TODO: call use case here
        }
    }

    // Required by AppBuilder to add this view to the CardLayout
    public JPanel getPanel() {
        return mainPanel;
    }
}