package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import app.AppBuilder;
import interface_adapter.controller.ChangePasswordController;
import interface_adapter.controller.LogoutController;
import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.SettingsState;
import interface_adapter.view_model.SettingsViewModel;

/**
 * View for Application settings.
 */
public class SettingsView extends StatefulView<SettingsState> {

    private ChangePasswordController changePasswordController;
    private LogoutController logoutController;

    private final DashboardViewModel dashboardViewModel;

    private final JPasswordField oldPasswordInputField = new JPasswordField(15);
    private final JPasswordField newPasswordInputField = new JPasswordField(15);
    private final JPasswordField confirmPasswordInputField = new JPasswordField(15);
    private final JLabel changePasswordErrorField = new JLabel();
    private final JLabel userEmailLabel = new JLabel();

    public SettingsView(SettingsViewModel settingsViewModel, DashboardViewModel dashboardViewModel) {
        super("settings", settingsViewModel);
        this.dashboardViewModel = dashboardViewModel;

        final JPanel viewHeader = getHeaderPanel();

        // Main content panel with GridLayout for equal-width columns
        final JPanel mainContent = new JPanel(new GridLayout(1, 2));
        mainContent.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Left side - Navigation buttons (wrapper for centering)
        final JPanel leftWrapper = new JPanel(new BorderLayout());
        final JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        userEmailLabel.setFont(new Font(null, Font.BOLD, 16));
        userEmailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JButton manageUploadedFilesButton = new JButton("Manage Uploaded Files");
        manageUploadedFilesButton.addActionListener(event -> {
            AppBuilder.viewManagerModel.setView("uploadMaterials");
        });
        manageUploadedFilesButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(userEmailLabel);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(manageUploadedFilesButton);

        leftWrapper.add(leftPanel, BorderLayout.NORTH);

        // Right side - Change password form (wrapper for centering)
        final JPanel rightWrapper = new JPanel(new BorderLayout());
        final JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        final JLabel changePasswordTitle = new JLabel("Change Password");
        changePasswordTitle.setFont(new Font(null, Font.BOLD, 16));
        changePasswordTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel oldPasswordInfo = new LabelTextPanel(
                new JLabel("Current Password"), oldPasswordInputField);
        final LabelTextPanel newPasswordInfo = new LabelTextPanel(
                new JLabel("New Password"), newPasswordInputField);
        final LabelTextPanel confirmPasswordInfo = new LabelTextPanel(
                new JLabel("Confirm Password"), confirmPasswordInputField);

        // Style the error label
        changePasswordErrorField.setForeground(Color.RED);
        changePasswordErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JButton changePasswordButton = getChangePasswordButton();

        // Add document listeners for password fields
        addPasswordFieldListener(oldPasswordInputField, SettingsState::setOldPassword);
        addPasswordFieldListener(newPasswordInputField, SettingsState::setNewPassword);
        addPasswordFieldListener(confirmPasswordInputField, SettingsState::setConfirmPassword);

        rightPanel.add(changePasswordTitle);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(oldPasswordInfo);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(newPasswordInfo);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(confirmPasswordInfo);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(changePasswordErrorField);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(changePasswordButton);

        rightWrapper.add(rightPanel, BorderLayout.NORTH);

        mainContent.add(leftWrapper);
        mainContent.add(rightWrapper);

        // Bottom panel with centered Return button
        final JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        final JButton returnButton = new JButton("Return");
        returnButton.addActionListener(event -> {
            // Clear form when navigating away
            viewModel.setState(new SettingsState());
            viewModel.firePropertyChange();
            AppBuilder.viewManagerModel.setView("dashboard");
        });
        bottomPanel.add(returnButton);

        this.add(viewHeader, BorderLayout.NORTH);
        this.add(mainContent, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        // Update user email when view becomes visible
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                final String userEmail = dashboardViewModel.getState().getEmail();
                if (userEmail.isEmpty()) {
                    userEmailLabel.setText("<no user found>");
                }
                else {
                    userEmailLabel.setText(dashboardViewModel.getState().getEmail());
                }
            }
        });
    }

    private JPanel getHeaderPanel() {
        final JPanel viewHeader = new ViewHeader("Settings");

        final JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(event -> {
            if (logoutController != null) {
                final int logoutConfirmOptionPane = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to log out?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION);

                if (logoutConfirmOptionPane == JOptionPane.YES_OPTION) {
                    logoutController.execute();
                }
            }
        });
        viewHeader.add(logoutButton, BorderLayout.EAST);
        return viewHeader;
    }

    private JButton getChangePasswordButton() {
        final JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.addActionListener(event -> {
            if (changePasswordController != null) {
                final SettingsState state = viewModel.getState();

                changePasswordController.execute(
                        dashboardViewModel.getState().getUserId(),
                        state.getOldPassword(),
                        state.getNewPassword(),
                        state.getConfirmPassword());
            }
        });
        changePasswordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        return changePasswordButton;
    }

    private void addPasswordFieldListener(JPasswordField field,
                                          java.util.function.BiConsumer<SettingsState, String> setter) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final SettingsState currentState = viewModel.getState();
                setter.accept(currentState, new String(field.getPassword()));
                viewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final SettingsState state = (SettingsState) evt.getNewValue();

            // Update password fields if they've been cleared
            if (state.getOldPassword().isEmpty()) {
                oldPasswordInputField.setText("");
            }
            if (state.getNewPassword().isEmpty()) {
                newPasswordInputField.setText("");
            }
            if (state.getConfirmPassword().isEmpty()) {
                confirmPasswordInputField.setText("");
            }

            // Display error message if present
            if (state.getChangePasswordError() != null && !state.getChangePasswordError().isEmpty()) {
                changePasswordErrorField.setText(state.getChangePasswordError());
            }
            else {
                changePasswordErrorField.setText("");
            }
        }
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

}