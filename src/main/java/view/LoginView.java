package view;

import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import app.AppBuilder;
import interface_adapter.controller.LoginController;
import interface_adapter.view_model.LoginState;
import interface_adapter.view_model.LoginViewModel;

/**
 * The View for when the user is logging into the program.
 */
public class LoginView extends StatefulView<LoginState> {

    private final JTextField emailInputField = new JTextField(15);
    private final JLabel loginErrorField = new JLabel();

    private final JPasswordField passwordInputField = new JPasswordField(15);

    private LoginController loginController;

    public LoginView(LoginViewModel loginViewModel) {
        super("login", loginViewModel);

        final JLabel title = new JLabel("Login Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel emailInfo = new LabelTextPanel(
                new JLabel("Email"), emailInputField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel("Password"), passwordInputField);

        // Style the error label
        loginErrorField.setForeground(Color.RED);
        loginErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel buttons = getButtonsPanel();

        emailInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final LoginState currentState = viewModel.getState();
                currentState.setEmail(emailInputField.getText());
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

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final LoginState currentState = viewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
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

        this.add(title);
        this.add(emailInfo);
        this.add(passwordInfo);
        this.add(loginErrorField);
        this.add(buttons);
    }

    private JPanel getButtonsPanel() {
        final JPanel buttons = new JPanel();

        final JButton loginButton = new JButton("log In");
        loginButton.addActionListener(event -> {
            final LoginState state = viewModel.getState();
            loginController.execute(state.getEmail(), state.getPassword());
        });
        buttons.add(loginButton);

        final JButton returnButton = new JButton("Return");
        returnButton.addActionListener(event -> {
            // Clear form when navigating away
            viewModel.setState(new LoginState());
            viewModel.firePropertyChange();
            AppBuilder.viewManagerModel.setView("initial");
        });
        buttons.add(returnButton);
        return buttons;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final LoginState state = (LoginState) evt.getNewValue();

            // Clear input fields if state has been reset
            if (state.getEmail().isEmpty()) {
                emailInputField.setText("");
            }
            if (state.getPassword().isEmpty()) {
                passwordInputField.setText("");
            }

            // Display error message if present
            if (state.getEmailError() != null && !state.getEmailError().isEmpty()) {
                loginErrorField.setText(state.getEmailError());
            }
            else {
                loginErrorField.setText("");
            }
        }
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}