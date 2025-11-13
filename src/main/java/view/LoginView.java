package view;

import interface_adapter.view_model.LoginViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for user login and account creation.
 * Observes the LoginViewModel and updates the UI accordingly.
 */
public class LoginView extends JPanel implements PropertyChangeListener {
	private final LoginViewModel viewModel;
	
	private JTextField emailField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton signUpButton;
	private JButton googleSignInButton;
	private JLabel errorLabel;
	
	public LoginView(LoginViewModel viewModel) {
		this.viewModel = viewModel;
		this.viewModel.addPropertyChangeListener(this);
		
		initializeComponents();
		layoutComponents();
	}
	
	private void initializeComponents() {
		emailField = new JTextField(20);
		passwordField = new JPasswordField(20);
		loginButton = new JButton("Login");
		signUpButton = new JButton("Sign Up");
		googleSignInButton = new JButton("Sign in with Google");
		errorLabel = new JLabel(" ");
		errorLabel.setForeground(Color.RED);
	}
	
	private void layoutComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		
		// TODO: Complete the layout
		// Add email field, password field, buttons, and error label
		// Use GridBagLayout constraints to position components
	}
	
	public String getEmail() {
		return emailField.getText();
	}
	
	public String getPassword() {
		return new String(passwordField.getPassword());
	}
	
	public void setLoginButtonListener(java.awt.event.ActionListener listener) {
		loginButton.addActionListener(listener);
	}
	
	public void setSignUpButtonListener(java.awt.event.ActionListener listener) {
		signUpButton.addActionListener(listener);
	}
	
	public void setGoogleSignInButtonListener(java.awt.event.ActionListener listener) {
		googleSignInButton.addActionListener(listener);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// Update view based on ViewModel changes
		if ("errorMessage".equals(evt.getPropertyName())) {
			String error = viewModel.getErrorMessage();
			errorLabel.setText(error.isEmpty() ? " " : error);
		} else if ("loginInProgress".equals(evt.getPropertyName())) {
			boolean inProgress = viewModel.isLoginInProgress();
			loginButton.setEnabled(!inProgress);
			signUpButton.setEnabled(!inProgress);
			googleSignInButton.setEnabled(!inProgress);
		}
	}
}

