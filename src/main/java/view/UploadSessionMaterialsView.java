package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UploadSessionMaterialsView extends JFrame {

    private JPanel mainPanel;
    private JButton uploadButton;
    private JButton cancelButton;
    private JLabel titleLabel;

    public UploadSessionMaterialsView() {
        // Initialize components
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        titleLabel = new JLabel("Upload Session Materials", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        uploadButton = new JButton("Upload");
        cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(uploadButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set content pane and default frame properties
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // centers the JFrame
        setVisible(true);

        // Example button actions
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(UploadSessionMaterialsView.this, "Upload clicked!");
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // closes the window
            }
        });
    }

    // Test main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UploadSessionMaterialsView());
    }
}