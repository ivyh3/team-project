package view;

import interface_adapter.login.InitialViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class InitialView extends JPanel {

    private final InitialViewModel viewModel;
    private final JButton loginButton;
    private final JButton signupButton;

    public InitialView(InitialViewModel viewModel) {
        this.viewModel = viewModel;

        // Main layout (vertical stacking for label + buttons panel)
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome! Choose an option:");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create a horizontal panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0)); // spacing = 20px

        loginButton = new JButton("Log In");
        signupButton = new JButton("Sign Up");

        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        // Add to main panel
        this.add(Box.createVerticalStrut(20)); // spacing on top
        this.add(title);
        this.add(Box.createVerticalStrut(20)); // spacing between label and buttons
        this.add(buttonPanel);
    }

    public void addLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addSignupButtonListener(ActionListener listener) {
        signupButton.addActionListener(listener);
    }

    public String getViewName() {
        return "initial";
    }
}
