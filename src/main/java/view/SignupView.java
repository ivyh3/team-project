package view;

import app.AppBuilder;
import interface_adapter.controller.SignupController;
import interface_adapter.view_model.SignupState;
import interface_adapter.view_model.SignupViewModel;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.beans.PropertyChangeEvent;

/**
 * The View for the Signup Use Case.
 */
public class SignupView extends StatefulView<SignupState> {
    private final JTextField emailInputField = new JTextField(15);
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(15);
    private final JLabel signupErrorField = new JLabel();
    private SignupController signupController;

    private final JButton signupButton;
    private final JButton returnButton;
    private final JButton toLoginButton;

    public SignupView(SignupViewModel signupViewModel) {
        super("sign up", signupViewModel);

        final JLabel title = new JLabel(SignupViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel emailInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.EMAIL_LABEL), emailInputField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.PASSWORD_LABEL), passwordInputField);
        final LabelTextPanel repeatPasswordInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.REPEAT_PASSWORD_LABEL), repeatPasswordInputField);

        // Style the error label
        signupErrorField.setForeground(Color.RED);
        signupErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel buttons = new JPanel();

        toLoginButton = new JButton(SignupViewModel.TO_LOGIN_BUTTON_LABEL);
        toLoginButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("login");
        });
        buttons.add(toLoginButton);

        signupButton = new JButton(SignupViewModel.SIGNUP_BUTTON_LABEL);
        signupButton.addActionListener(e -> {
            SignupState state = viewModel.getState();
            signupController.execute(
                    state.getEmail(),
                    state.getPassword(),
                    state.getRepeatPassword());
        });
        buttons.add(signupButton);

        returnButton = new JButton(SignupViewModel.CANCEL_BUTTON_LABEL);
        returnButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("initial");
        });
        buttons.add(returnButton);

        addEmailListener();
        addPasswordListener();
        addRepeatPasswordListener();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(title);
        this.add(emailInfo);
        this.add(passwordInfo);
        this.add(repeatPasswordInfo);
        this.add(signupErrorField);
        this.add(buttons);
    }

    private void addEmailListener() {
        emailInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = viewModel.getState();
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
    }

    private void addPasswordListener() {
        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = viewModel.getState();
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
    }

    private void addRepeatPasswordListener() {
        repeatPasswordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = viewModel.getState();
                currentState.setRepeatPassword(new String(repeatPasswordInputField.getPassword()));
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
            final SignupState state = (SignupState) evt.getNewValue();
            // Display error message if present
            if (state.getEmailError() != null && !state.getEmailError().isEmpty()) {
                signupErrorField.setText(state.getEmailError());
            } else {
                signupErrorField.setText("");
            }
        }
    }

    public void setSignupController(SignupController controller) {
        this.signupController = controller;
    }
}
