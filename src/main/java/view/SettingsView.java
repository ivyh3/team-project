package view;

import app.AppBuilder;
import interface_adapter.controller.ChangePasswordController;
import interface_adapter.controller.LogoutController;
import interface_adapter.view_model.DashboardState;
import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.SettingsState;
import interface_adapter.view_model.SettingsViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;

/**
 * View for Application settings.
 */
public class SettingsView extends StatefulView<SettingsState> {

    private ChangePasswordController changePasswordController;
    private LogoutController logoutController;
    private DashboardViewModel dashboardViewModel;

    private final JPasswordField oldPasswordInputField = new JPasswordField(15);
    private final JPasswordField newPasswordInputField = new JPasswordField(15);
    private final JPasswordField confirmPasswordInputField = new JPasswordField(15);
    private final JLabel changePasswordErrorField = new JLabel();

    private final JButton manageUploadedFilesButton;
    private final JButton changePasswordButton;
    private final JButton logoutButton;
    private final JButton returnButton;

    public SettingsView(SettingsViewModel settingsViewModel, DashboardViewModel dashboardViewModel) {
        super("settings", settingsViewModel);
        this.dashboardViewModel = dashboardViewModel;

        JPanel viewHeader = new ViewHeader("Settings");

        logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(e -> {
            if (logoutController != null) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to log out?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    logoutController.execute();
                }
            }
        });
        viewHeader.add(logoutButton, BorderLayout.EAST);

        // Main content panel with BorderLayout
        JPanel mainContent = new JPanel(new BorderLayout(20, 0));

        // Left side - Navigation buttons
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel navigationTitle = new JLabel("Navigation");
        navigationTitle.setFont(new Font(null, Font.BOLD, 16));
        navigationTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        manageUploadedFilesButton = new JButton("Manage Uploaded Files");
        manageUploadedFilesButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("uploadMaterials");
        });
        manageUploadedFilesButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        returnButton = new JButton("Return to Dashboard");
        returnButton.addActionListener(e -> {
            // Clear form when navigating away
            viewModel.setState(new SettingsState());
            viewModel.firePropertyChange();
            AppBuilder.viewManagerModel.setView("dashboard");
        });
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(navigationTitle);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(manageUploadedFilesButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(returnButton);
        leftPanel.add(Box.createVerticalGlue());

        // Right side - Change password form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel changePasswordTitle = new JLabel("Change Password");
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

        changePasswordButton = new JButton("Change Password");
        changePasswordButton.addActionListener(e -> {
            if (changePasswordController != null) {
                SettingsState state = viewModel.getState();

                // Get userId from DashboardState
                DashboardState dashboardState = dashboardViewModel.getState();
                String userId = dashboardState.getUserId();

                changePasswordController.execute(
                        userId,
                        state.getOldPassword(),
                        state.getNewPassword(),
                        state.getConfirmPassword());
            }
        });
        changePasswordButton.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        rightPanel.add(Box.createVerticalGlue());

        mainContent.add(leftPanel, BorderLayout.WEST);
        mainContent.add(rightPanel, BorderLayout.CENTER);

        this.add(viewHeader, BorderLayout.NORTH);
        this.add(mainContent, BorderLayout.CENTER);
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
            } else {
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
