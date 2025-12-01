package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import app.AppBuilder;

/**
 * The Initial View - the first screen users see.
 * Provides options to log in or sign up.
 */
public class InitialView extends View {
    public InitialView() {
        super("initial");

        final JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel("Welcome to AI Study Companion!");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font(null, Font.BOLD, 24));

        final JLabel subtitle = new JLabel("Choose an option to get started:");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font(null, Font.PLAIN, 14));

        // Create a horizontal panel for the buttons
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        final JButton loginButton = new JButton("Log In");
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.addActionListener(event -> {
            AppBuilder.viewManagerModel.setView("login");
        });

        final JButton signupButton = new JButton("Sign Up");
        signupButton.setPreferredSize(new Dimension(120, 40));
        signupButton.addActionListener(event -> {
            AppBuilder.viewManagerModel.setView("sign up");
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        // Add to main panel
        main.add(Box.createVerticalGlue());
        main.add(title);
        main.add(Box.createVerticalStrut(10));
        main.add(subtitle);
        main.add(Box.createVerticalStrut(30));
        main.add(buttonPanel);
        main.add(Box.createVerticalGlue());

        this.add(main, BorderLayout.CENTER);
    }
}
