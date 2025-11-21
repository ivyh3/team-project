package view;

import interface_adapter.controller.StartStudySessionController;
import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionConfigState.SessionType;
import interface_adapter.view_model.StudySessionConfigViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
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
            startStudySessionController.execute(currentConfig);
        });

        navigationContainer.add(cancelButton);
        navigationContainer.add(Box.createRigidArea(new Dimension(40, 0)));
        navigationContainer.add(nextButton);

        main.add(selectTypePanel);
        main.add(selectDurationPanel);
        main.add(selectReferencePanel);
        main.add(navigationContainer);

        updateDurationPanelVisibility(viewModel.getState().getSessionType());
        updateFileSelector(viewModel.getState().getFileOptions());

        this.add(viewHeader, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);

        attachListeners();
        setFields(viewModel.getState());
    }

    /**
     * Updates whether the panel to change the duration hours/minute options are visible in the view,
     * depending on the currently selected session type.
     *
     * @param sessionType The current session type selected.
     */
    private void updateDurationPanelVisibility(SessionType sessionType) {

        selectDurationPanel.setVisible(sessionType == SessionType.FIXED);
    }

    private JPanel buildChooseDurationPanel() {
        JPanel chooseDurationPanel = new JPanel();
        chooseDurationPanel.setLayout(new GridLayout(1, 2));
        chooseDurationPanel.setBorder(BorderFactory.createTitledBorder("Session Duration"));

        JPanel hoursContainer = new JPanel();
        hoursContainer.setLayout(new BoxLayout(hoursContainer, BoxLayout.X_AXIS));
        hoursContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hoursLabel = new JLabel("Hours");
        hoursLabel.setFont(new Font(null, Font.BOLD, 24));

        hoursSelector.setModel(new SpinnerNumberModel(0, 0, 23, 1));
        hoursSelector.setEditor(new JSpinner.DefaultEditor(hoursSelector)); // Prevent manual edit of field
        hoursSelector.setMaximumSize(new Dimension(100, 30));
        hoursSelector.setFont(new Font(null, Font.PLAIN, 20));

        hoursContainer.add(hoursLabel);
        hoursContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        hoursContainer.add(hoursSelector);

        JPanel minutesContainer = new JPanel();
        minutesContainer.setLayout(new BoxLayout(minutesContainer, BoxLayout.X_AXIS));
        minutesContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel minutesLabel = new JLabel("Minutes");
        minutesLabel.setFont(new Font(null, Font.BOLD, 24));

        minutesSelector.setModel(new SpinnerNumberModel(0, 0, 55, 5));
        minutesSelector.setEditor(new JSpinner.DefaultEditor(minutesSelector)); // Prevent manual edit of field
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

        // Populate the type selector with the two session type options.
        typeSelector.addItem(VARIABLE_SESSION);
        typeSelector.addItem(TIMED_SESSION);
        typeSelector.setSelectedIndex(0);
        typeSelector.setFont(new Font(null, Font.PLAIN, 20));
        typeSelectorContainer.add(typeSelector);

        JPanel timedSessionContainer = new JPanel();
        timedSessionContainer.setLayout(new BoxLayout(timedSessionContainer, BoxLayout.Y_AXIS));

        JLabel timedSessionHeading = new JLabel(TIMED_SESSION);
        JLabel timedSessionLabel = new JLabel(FIXED_SESSION_DESCRIPTION);

        timedSessionHeading.setFont(new Font(null, Font.BOLD, 16));
        timedSessionLabel.setFont(new Font(null, Font.ITALIC, 12));

        timedSessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timedSessionHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        JPanel promptPanel = new JPanel();

        promptArea.setBorder(BorderFactory.createTitledBorder("What are you studying?"));
        promptPanel.add(promptArea);

        JPanel selectorPanel = new JPanel();
        selectorPanel.setBorder(BorderFactory.createTitledBorder("Textbook"));
        selectorPanel.add(fileSelector);

        chooseReferenceMaterialsPanel.add(promptPanel);
        chooseReferenceMaterialsPanel.add(selectorPanel);

        return chooseReferenceMaterialsPanel;
    }

    /**
     * Attaches listeners to input elements to update state on change.
     */
    private void attachListeners() {
        typeSelector.addActionListener(e -> {
            String choice = (String) typeSelector.getSelectedItem();
            if (choice == null) return;

            SessionType sessionType = choice.equals(VARIABLE_SESSION) ? SessionType.VARIABLE : SessionType.FIXED;
            viewModel.getState().setSessionType(sessionType);

            // Must make the presenter fire property changed so the view can react and
            // show the duration selector if needed.
            if (startStudySessionController != null) {
                startStudySessionController.setSessionType(sessionType);
            }
        });

        fileSelector.addActionListener(e -> {
            String choice = (String) fileSelector.getSelectedItem();
            if (choice != null) {
                viewModel.getState().setReferenceFile(choice);
            }
        });

        hoursSelector.addChangeListener(e -> {
            Integer hours = (Integer) hoursSelector.getValue();
            viewModel.getState().setTargetDurationHours(hours);
        });

        minutesSelector.addChangeListener(e -> {

            Integer minutes = (Integer) minutesSelector.getValue();
            viewModel.getState().setTargetDurationMinutes(minutes);
        });

        promptArea.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                viewModel.getState().setPrompt(promptArea.getText());
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

    /**
     * Sets all editable fields to synchronize with the provided state.
     *
     * @param state The state for the view to sync with.
     */
    private void setFields(StudySessionConfigState state) {
        typeSelector.setSelectedItem(state.getSessionType() == StudySessionConfigState.SessionType.FIXED ? TIMED_SESSION : VARIABLE_SESSION);

        hoursSelector.setValue(state.getTargetDurationHours());
        minutesSelector.setValue(state.getTargetDurationMinutes());
        promptArea.setText(state.getPrompt());

        updateFileSelector(state.getFileOptions());
        if (state.getReferenceFile() != null) {
            fileSelector.setSelectedItem(state.getReferenceFile());
        }

        updateDurationPanelVisibility(state.getSessionType());
    }

    /**
     * Populates the file selector with the given file options.
     *
     * @param fileOptions the options the user can choose from.
     */
    private void updateFileSelector(List<String> fileOptions) {
        fileSelector.removeAllItems();
        if (fileOptions != null) {
            for (String fileName : fileOptions) {
                fileSelector.addItem(fileName);
            }
        }
    }

    /**
     * Sets the controller to start the study session.
     *
     * @param startStudySessionController The controller to use.
     */
    public void setStartStudySessionController(StartStudySessionController startStudySessionController) {
        this.startStudySessionController = startStudySessionController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("state")) {
            StudySessionConfigState state = (StudySessionConfigState) evt.getNewValue();
            System.out.println(state);
            setFields(state);
        } else if (evt.getPropertyName().equals("error")) {
            StudySessionConfigState state = (StudySessionConfigState) evt.getNewValue();
            String error = state.getError();
            JOptionPane.showMessageDialog(this, error, "Configuration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
