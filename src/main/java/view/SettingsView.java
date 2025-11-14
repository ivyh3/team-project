package view;

import app.AppBuilder;
import interface_adapter.view_model.SettingsViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for Application settings.
 *
 * NOTE: THIS DOES NOT NEED STATE, IS JUST A EXAMPLE OF USING STATE FOR A VIEW
 */
public class SettingsView extends StatefulView<Boolean> implements PropertyChangeListener {

    private final JLabel testComponentStateLabel;

    public SettingsView(SettingsViewModel settingsViewModel) {
        super("settings", settingsViewModel);


        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        JPanel header = new ViewHeader("Settings");

        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("dashboard");
        });
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel settingsContainer = new JPanel();

        JButton manageUploadedFilesButton = new JButton("Manage Uploaded Files");
        manageUploadedFilesButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("fileManager");
        });

        JButton linkGoogleCalendarButton = new JButton("Link Google Calendar");
        linkGoogleCalendarButton.addActionListener(e -> {
            AppBuilder.viewManagerModel.setView("linkAccountPrompt");
        });

        settingsContainer.add(linkGoogleCalendarButton);
        settingsContainer.add(manageUploadedFilesButton);

        // FOR TESTING STATE UPDATES
        JButton toggleSetting = new JButton("Toggle Setting");
        testComponentStateLabel = new JLabel("Current Setting: " + settingsViewModel.getState());
        toggleSetting.addActionListener(e -> {
            System.out.println("Toggle button clicked.");
            viewModel.setState(!viewModel.getState());
        });

        main.add(settingsContainer);
        main.add(Box.createVerticalGlue());
        main.add(returnButton);

        main.add(toggleSetting);
        main.add(testComponentStateLabel);

        this.add(header, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            System.out.println(viewModel.getState());
            testComponentStateLabel.setText("Current Setting: " + viewModel.getState());

        }
    }

}
