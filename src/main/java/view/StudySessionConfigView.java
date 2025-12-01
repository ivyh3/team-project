package view;

import interface_adapter.controller.StartStudySessionController;
import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionConfigState.SessionType;
import interface_adapter.view_model.StudySessionConfigViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.List;

/**
 * The view for configuring, and starting a study session.
 */
public class StudySessionConfigView extends StatefulView<StudySessionConfigState> {
    public static final String VARIABLE_SESSION = "Variable Session";
    public static final String TIMED_SESSION = "Timed Session";
    public static final String VARIABLE_SESSION_DESCRIPTION = "Study for as long as you want until you're ready.";
    public static final String FIXED_SESSION_DESCRIPTION = "Specify a quantity of studying time.";

    private final JPanel selectDurationPanel;
    private final JSpinner hoursSelector = new JSpinner();
    private final JSpinner minutesSelector = new JSpinner();
    private final JTextArea promptArea = new JTextArea(4, 40);
    private final JComboBox<String> typeSelector = new JComboBox<>();
    private final JComboBox<String> fileSelector = new JComboBox<>();

    private StartStudySessionController startStudySessionController;

    public StudySessionConfigView(StudySessionConfigViewModel viewModel) {
        super("studySessionConfig", viewModel);

        JPanel viewHeader = new ViewHeader("Session Config");
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        JPanel selectTypePanel = buildChooseTypePanel();
        selectDurationPanel = buildChooseDurationPanel();
        JPanel selectReferencePanel = buildChooseReferencePanel();

        JPanel navigationContainer = new JPanel();
        navigationContainer.setLayout(new BoxLayout(navigationContainer, BoxLayout.X_AXIS));

        JButton cancelButton = new JButton("Cancel");
        JButton nextButton = new JButton("Next");

        cancelButton.addActionListener(e -> startStudySessionController.abortStudySessionConfig());
        nextButton.addActionListener(e -> {
            StudySessionConfigState currentConfig = viewModel.getState().copy();

            // Ensure referenceFile is set from either combo box or uploaded file
            String selectedFile = viewModel.getState().getReferenceFile();
            if (selectedFile == null || selectedFile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select or upload a reference file.",
                        "No File Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Pass the full file path to the controller
            startStudySessionController.execute(currentConfig);
        });

        navigationContainer.add(cancelButton);
        navigationContainer.add(Box.createRigidArea(new Dimension(40, 0)));
        navigationContainer.add(nextButton);

        main.add(selectTypePanel);
        main.add(selectDurationPanel);
        main.add(selectReferencePanel);
        main.add(navigationContainer);

        // Refresh file list button
        JButton refreshButton = new JButton("Refresh File List");
        refreshButton.addActionListener(e -> {
            if (startStudySessionController != null) {
                startStudySessionController.refreshFileOptions();
            }
        });

        viewHeader.add(refreshButton, BorderLayout.EAST);

        // Initialize view on initial state
        updateDurationPanelVisibility(viewModel.getState().getSessionType());
        updateFileSelector(viewModel.getState().getFileOptions());
        attachListeners();
        setFields(viewModel.getState());

        this.add(viewHeader, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
    }

    private void updateDurationPanelVisibility(SessionType sessionType) {
        selectDurationPanel.setVisible(sessionType == SessionType.FIXED);
    }

    private JPanel buildChooseDurationPanel() {
        JPanel chooseDurationPanel = new JPanel();
        chooseDurationPanel.setLayout(new GridLayout(1, 2));
        chooseDurationPanel.setBorder(BorderFactory.createTitledBorder("Session Duration"));

        JPanel hoursContainer = new JPanel();
        hoursContainer.setLayout(new BoxLayout(hoursContainer, BoxLayout.X_AXIS));
        JLabel hoursLabel = new JLabel("Hours");
        hoursLabel.setFont(new Font(null, Font.BOLD, 24));
        hoursSelector.setModel(new SpinnerNumberModel(0, 0, 23, 1));
        hoursSelector.setEditor(new JSpinner.DefaultEditor(hoursSelector));
        hoursSelector.setMaximumSize(new Dimension(100, 30));
        hoursSelector.setFont(new Font(null, Font.PLAIN, 20));
        hoursContainer.add(hoursLabel);
        hoursContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        hoursContainer.add(hoursSelector);

        JPanel minutesContainer = new JPanel();
        minutesContainer.setLayout(new BoxLayout(minutesContainer, BoxLayout.X_AXIS));
        JLabel minutesLabel = new JLabel("Minutes");
        minutesLabel.setFont(new Font(null, Font.BOLD, 24));
        minutesSelector.setModel(new SpinnerNumberModel(0, 0, 55, 5));
        minutesSelector.setEditor(new JSpinner.DefaultEditor(minutesSelector));
        minutesSelector.setMaximumSize(new Dimension(100, 30));
        minutesSelector.setFont(new Font(null, Font.PLAIN, 20));
        minutesContainer.add(minutesLabel);
        minutesContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        minutesContainer.add(minutesSelector);

        chooseDurationPanel.add(hoursContainer);
        chooseDurationPanel.add(minutesContainer);

        return chooseDurationPanel;
    }

    private JPanel buildChooseTypePanel() {
        JPanel chooseSessionTypePanel = new JPanel();
        chooseSessionTypePanel.setBorder(BorderFactory.createTitledBorder("Session Type"));
        chooseSessionTypePanel.setLayout(new GridLayout(1, 2));

        JPanel typeSelectorContainer = new JPanel();
        typeSelector.addItem(VARIABLE_SESSION);
        typeSelector.addItem(TIMED_SESSION);
        typeSelector.setSelectedIndex(0);
        typeSelector.setFont(new Font(null, Font.PLAIN, 20));
        typeSelectorContainer.add(typeSelector);

        // Descriptions
        JPanel timedSessionContainer = new JPanel();
        timedSessionContainer.setLayout(new BoxLayout(timedSessionContainer, BoxLayout.Y_AXIS));
        JLabel timedSessionHeading = new JLabel(TIMED_SESSION);
        JLabel timedSessionLabel = new JLabel(FIXED_SESSION_DESCRIPTION);
        timedSessionHeading.setFont(new Font(null, Font.BOLD, 16));
        timedSessionLabel.setFont(new Font(null, Font.ITALIC, 12));
        timedSessionHeading.setAlignmentX(Component.CENTER_ALIGNMENT);
        timedSessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timedSessionContainer.add(timedSessionHeading);
        timedSessionContainer.add(timedSessionLabel);

        JPanel variableSessionContainer = new JPanel();
        variableSessionContainer.setLayout(new BoxLayout(variableSessionContainer, BoxLayout.Y_AXIS));
        JLabel variableSessionHeading = new JLabel(VARIABLE_SESSION);
        JLabel variableSessionLabel = new JLabel(VARIABLE_SESSION_DESCRIPTION);
        variableSessionHeading.setFont(new Font(null, Font.BOLD, 16));
        variableSessionLabel.setFont(new Font(null, Font.ITALIC, 12));
        variableSessionHeading.setAlignmentX(Component.CENTER_ALIGNMENT);
        variableSessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        variableSessionContainer.add(variableSessionHeading);
        variableSessionContainer.add(variableSessionLabel);

        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        descriptionPanel.add(Box.createVerticalGlue());
        descriptionPanel.add(timedSessionContainer);
        descriptionPanel.add(Box.createVerticalGlue());
        descriptionPanel.add(variableSessionContainer);
        descriptionPanel.add(Box.createVerticalGlue());

        chooseSessionTypePanel.add(typeSelectorContainer);
        chooseSessionTypePanel.add(descriptionPanel);

        return chooseSessionTypePanel;
    }

    private JPanel buildChooseReferencePanel() {
        JPanel chooseReferenceMaterialsPanel = new JPanel();
        chooseReferenceMaterialsPanel.setLayout(new BoxLayout(chooseReferenceMaterialsPanel, BoxLayout.X_AXIS));
        chooseReferenceMaterialsPanel.setBorder(BorderFactory.createTitledBorder("Study Session Context"));

        // Prompt panel
        JPanel promptPanel = new JPanel();
        promptArea.setBorder(BorderFactory.createTitledBorder("What are you studying?"));
        promptPanel.add(promptArea);

        // Reference file panel
        JPanel selectorPanel = new JPanel();
        selectorPanel.setLayout(new BoxLayout(selectorPanel, BoxLayout.Y_AXIS));
        selectorPanel.setBorder(BorderFactory.createTitledBorder("Reference File"));

        // Existing combo box
        selectorPanel.add(fileSelector);

        // Add a file chooser button
        JButton chooseFileButton = new JButton("Choose File...");
        chooseFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                viewModel.getState().setReferenceFile(selectedFilePath);

                // Optional: show chosen file in combo box
                if (((DefaultComboBoxModel<String>) fileSelector.getModel()).getIndexOf(selectedFilePath) == -1) {
                    fileSelector.addItem(selectedFilePath);
                }
                fileSelector.setSelectedItem(selectedFilePath);
            }
        });
        selectorPanel.add(chooseFileButton);

        chooseReferenceMaterialsPanel.add(promptPanel);
        chooseReferenceMaterialsPanel.add(selectorPanel);

        return chooseReferenceMaterialsPanel;
    }


    private void attachListeners() {
        typeSelector.addActionListener(e -> {
            String choice = (String) typeSelector.getSelectedItem();
            if (choice == null) return;
            SessionType sessionType = choice.equals(VARIABLE_SESSION) ? SessionType.VARIABLE : SessionType.FIXED;
            viewModel.getState().setSessionType(sessionType);
            viewModel.firePropertyChange();
        });

        fileSelector.addActionListener(e -> {
            String selectedFile = (String) fileSelector.getSelectedItem();
            if (selectedFile != null) {
                viewModel.getState().setReferenceFile(selectedFile);
            }
        });

        hoursSelector.addChangeListener(e -> viewModel.getState().setTargetDurationHours((Integer) hoursSelector.getValue()));
        minutesSelector.addChangeListener(e -> viewModel.getState().setTargetDurationMinutes((Integer) minutesSelector.getValue()));

        promptArea.getDocument().addDocumentListener(new DocumentListener() {
            private void updatePrompt() { viewModel.getState().setPrompt(promptArea.getText()); }
            @Override public void insertUpdate(DocumentEvent e) { updatePrompt(); }
            @Override public void removeUpdate(DocumentEvent e) { updatePrompt(); }
            @Override public void changedUpdate(DocumentEvent e) { updatePrompt(); }
        });
    }

    private void setFields(StudySessionConfigState state) {
        typeSelector.setSelectedItem(state.getSessionType() == SessionType.FIXED ? TIMED_SESSION : VARIABLE_SESSION);
        hoursSelector.setValue(state.getTargetDurationHours());
        minutesSelector.setValue(state.getTargetDurationMinutes());
        promptArea.setText(state.getPrompt());
        updateFileSelector(state.getFileOptions());
        if (state.getReferenceFile() != null) {
            fileSelector.setSelectedItem(state.getReferenceFile());
        }
        updateDurationPanelVisibility(state.getSessionType());
    }

    private void updateFileSelector(List<String> fileOptions) {
        if (fileOptions == null) return;

        ActionListener[] listeners = fileSelector.getActionListeners();
        ActionListener fileListener = listeners.length > 0 ? listeners[0] : null;
        if (fileListener != null) fileSelector.removeActionListener(fileListener);

        fileSelector.removeAllItems();
        for (String file : fileOptions) fileSelector.addItem(file);

        if (fileListener != null) fileSelector.addActionListener(fileListener);

        String selected = viewModel.getState().getReferenceFile();
        if (selected != null && !selected.isEmpty() && fileOptions.contains(selected)) {
            fileSelector.setSelectedItem(selected);
        } else if (!fileOptions.isEmpty()) {
            fileSelector.setSelectedIndex(0);
            viewModel.getState().setReferenceFile(fileSelector.getItemAt(0));
        }
    }

    public void setStartStudySessionController(StartStudySessionController controller) {
        this.startStudySessionController = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            setFields((StudySessionConfigState) evt.getNewValue());
        } else if (evt.getPropertyName().equals("error")) {
            JOptionPane.showMessageDialog(this, ((StudySessionConfigState) evt.getNewValue()).getError(), "Configuration Error", JOptionPane.ERROR_MESSAGE);
        } else if (evt.getPropertyName().equals("fileOptions")) {
            updateFileSelector(viewModel.getState().getFileOptions());
        }
    }
}