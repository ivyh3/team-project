package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jetbrains.annotations.NotNull;

import interface_adapter.controller.StartStudySessionController;
import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionConfigState.SessionType;
import interface_adapter.view_model.StudySessionConfigViewModel;

/**
 * The view for configuring, and starting a study session.
 */
public class StudySessionConfigView extends StatefulView<StudySessionConfigState> {
    public static final String VARIABLE_SESSION = "Variable Session";
    public static final String FIXED_SESSION = "Timed Session";
    public static final String VARIABLE_SESSION_DESCRIPTION = "Study for as long as you want until you're ready.";
    public static final String FIXED_SESSION_DESCRIPTION = "Specify a quantity of studying time.";
    public static final int TEXT_LG = 20;
    public static final int TEXT_BASE = 16;
    public static final int TEXT_SM = 12;
    public static final int TIME_SELECTOR_WIDTH = 100;
    public static final int TEXT_SELECTOR_HEIGHT = 30;
    public static final int MAX_MINS = 55;
    public static final int MINS_STEP = 5;
    public static final int HOUR_STEP = 1;
    public static final int MAX_HOUR = 23;
    public static final int GAP_SM = 10;
    public static final int GAP_LG = 40;
    private final JPanel selectDurationPanel;
    private final JSpinner hoursSelector = new JSpinner();
    private final JSpinner minutesSelector = new JSpinner();
    private final JTextArea promptArea = new JTextArea(4, 40);
    private final JComboBox<String> typeSelector = new JComboBox<>();
    private final JComboBox<String> fileSelector = new JComboBox<>();
    private final DashboardViewModel dashboardViewModel;
    private StartStudySessionController startStudySessionController;

    public StudySessionConfigView(StudySessionConfigViewModel viewModel, DashboardViewModel dashboardViewModel) {
        super("studySessionConfig", viewModel);
        this.dashboardViewModel = dashboardViewModel;
        final JPanel viewHeader = new ViewHeader("Session Config");
        final JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        final JPanel selectTypePanel = buildChooseTypePanel();
        selectDurationPanel = buildChooseDurationPanel();
        final JPanel selectReferencePanel = buildChooseReferencePanel();

        final JPanel navigationContainer = new JPanel();
        navigationContainer.setLayout(new BoxLayout(navigationContainer, BoxLayout.X_AXIS));

        final JButton cancelButton = new JButton("Cancel");
        final JButton nextButton = new JButton("Next");

        cancelButton.addActionListener(event -> startStudySessionController.abortStudySessionConfig());
        nextButton.addActionListener(event -> {
            final StudySessionConfigState currentConfig = viewModel.getState().copy();
            startStudySessionController.execute(
                this.dashboardViewModel.getState().getUserId(), currentConfig);
        });

        navigationContainer.add(cancelButton);
        navigationContainer.add(Box.createRigidArea(new Dimension(GAP_LG, 0)));
        navigationContainer.add(nextButton);

        main.add(selectTypePanel);
        main.add(selectDurationPanel);
        main.add(selectReferencePanel);
        main.add(navigationContainer);

        final JButton refreshButton = new JButton("Refresh File List");
        refreshButton.addActionListener(event -> {
                startStudySessionController.refreshFileOptions(
                    this.dashboardViewModel.getState().getUserId());
            }
        );

        viewHeader.add(refreshButton, BorderLayout.EAST);

        // Initialize view on initial state
        updateDurationPanelVisibility(viewModel.getState().getSessionType());
        updateFileSelector(viewModel.getState().getFileOptions());
        attachListeners();
        setFields(viewModel.getState());

        this.add(viewHeader, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
    }

    /**
     * Converts the choice string to a SessionType.
     *
     * @param choice the selected session type string
     * @return The equivalent SessionType object
     */
    @NotNull
    private static SessionType sessionStringToSessionType(String choice) {
        final SessionType sessionType;
        if (choice.equals(VARIABLE_SESSION)) {
            sessionType = SessionType.VARIABLE;
        }
        else {
            sessionType = SessionType.FIXED;
        }
        return sessionType;
    }

    /**
     * Updates whether the panel to change the duration hours/minute options are
     * visible in the view,
     * depending on the currently selected session type.
     *
     * @param sessionType The current session type selected.
     */
    private void updateDurationPanelVisibility(SessionType sessionType) {

        selectDurationPanel.setVisible(sessionType == SessionType.FIXED);
    }

    private JPanel buildChooseDurationPanel() {
        final JPanel chooseDurationPanel = new JPanel();
        chooseDurationPanel.setLayout(new GridLayout(1, 2));
        chooseDurationPanel.setBorder(BorderFactory.createTitledBorder("Session Duration"));

        final JPanel hoursContainer = new JPanel();
        hoursContainer.setLayout(new BoxLayout(hoursContainer, BoxLayout.X_AXIS));
        hoursContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel hoursLabel = new JLabel("Hours");
        hoursLabel.setFont(new Font(null, Font.BOLD, TEXT_LG));

        hoursSelector.setModel(new SpinnerNumberModel(0, 0, MAX_HOUR, HOUR_STEP));
        // Prevent manual edit of field
        hoursSelector.setEditor(new JSpinner.DefaultEditor(hoursSelector));
        hoursSelector.setMaximumSize(new Dimension(TIME_SELECTOR_WIDTH, TEXT_SELECTOR_HEIGHT));
        hoursSelector.setFont(new Font(null, Font.PLAIN, TEXT_LG));

        hoursContainer.add(hoursLabel);
        hoursContainer.add(Box.createRigidArea(new Dimension(GAP_SM, 0)));
        hoursContainer.add(hoursSelector);

        final JPanel minutesContainer = new JPanel();
        minutesContainer.setLayout(new BoxLayout(minutesContainer, BoxLayout.X_AXIS));
        minutesContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel minutesLabel = new JLabel("Minutes");
        minutesLabel.setFont(new Font(null, Font.BOLD, TEXT_LG));

        minutesSelector.setModel(new SpinnerNumberModel(0, 0, MAX_MINS, MINS_STEP));
        // Prevent manual edit of field
        minutesSelector.setEditor(new JSpinner.DefaultEditor(minutesSelector));
        minutesSelector.setMaximumSize(new Dimension(TIME_SELECTOR_WIDTH, TEXT_SELECTOR_HEIGHT));
        minutesSelector.setFont(new Font(null, Font.PLAIN, TEXT_LG));

        minutesContainer.add(minutesLabel);
        minutesContainer.add(Box.createRigidArea(new Dimension(GAP_SM, 0)));
        minutesContainer.add(minutesSelector);

        chooseDurationPanel.add(hoursContainer);
        chooseDurationPanel.add(minutesContainer);

        return chooseDurationPanel;
    }

    private JPanel buildChooseTypePanel() {
        final JPanel chooseSessionTypePanel = new JPanel();
        chooseSessionTypePanel.setBorder(BorderFactory.createTitledBorder("Session Type"));
        chooseSessionTypePanel.setLayout(new GridLayout(1, 2));

        final JPanel typeSelectorContainer = new JPanel();
        // Populate the type selector with the two session type options.
        typeSelector.addItem(VARIABLE_SESSION);
        typeSelector.addItem(FIXED_SESSION);
        typeSelector.setSelectedIndex(0);
        typeSelector.setFont(new Font(null, Font.PLAIN, TEXT_LG));
        typeSelectorContainer.add(typeSelector);

        final JPanel timedSessionContainer = new JPanel();
        timedSessionContainer.setLayout(new BoxLayout(timedSessionContainer, BoxLayout.Y_AXIS));

        final JLabel timedSessionHeading = new JLabel(FIXED_SESSION);
        final JLabel timedSessionLabel = new JLabel(FIXED_SESSION_DESCRIPTION);
        timedSessionHeading.setFont(new Font(null, Font.BOLD, TEXT_BASE));
        timedSessionLabel.setFont(new Font(null, Font.ITALIC, TEXT_SM));

        timedSessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timedSessionHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        timedSessionContainer.add(timedSessionHeading);
        timedSessionContainer.add(timedSessionLabel);

        final JPanel variableSessionContainer = new JPanel();
        variableSessionContainer.setLayout(new BoxLayout(variableSessionContainer, BoxLayout.Y_AXIS));

        final JLabel variableSessionHeading = new JLabel(VARIABLE_SESSION);
        final JLabel variableSessionLabel = new JLabel(VARIABLE_SESSION_DESCRIPTION);

        variableSessionHeading.setFont(new Font(null, Font.BOLD, TEXT_BASE));
        variableSessionLabel.setFont(new Font(null, Font.ITALIC, TEXT_SM));

        variableSessionHeading.setAlignmentX(Component.CENTER_ALIGNMENT);
        variableSessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        variableSessionContainer.add(variableSessionHeading);
        variableSessionContainer.add(variableSessionLabel);

        final JPanel descriptionPanel = new JPanel();
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
        final JPanel chooseReferenceMaterialsPanel = new JPanel();
        chooseReferenceMaterialsPanel.setLayout(new BoxLayout(chooseReferenceMaterialsPanel, BoxLayout.X_AXIS));
        chooseReferenceMaterialsPanel.setBorder(BorderFactory.createTitledBorder("Study Session Context"));

        final JPanel promptPanel = new JPanel();

        promptArea.setBorder(BorderFactory.createTitledBorder("What are you studying?"));
        promptPanel.add(promptArea);

        final JPanel selectorPanel = new JPanel();
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
        typeSelector.addActionListener(event -> {
            final String choice = (String) typeSelector.getSelectedItem();
            if (choice != null) {
                final SessionType sessionType = sessionStringToSessionType(choice);
                // Apparently as per the prof, I can bypass the CA engine for this because no logic is being done.
                viewModel.getState().setSessionType(sessionType);
                viewModel.firePropertyChange();
            }
        });

        fileSelector.addActionListener(event -> {
            final String choice = (String) fileSelector.getSelectedItem();
            if (choice != null) {
                viewModel.getState().setReferenceFile(choice);
            }
        });

        hoursSelector.addChangeListener(event -> {
            final Integer hours = (Integer) hoursSelector.getValue();
            viewModel.getState().setTargetDurationHours(hours);
        });

        minutesSelector.addChangeListener(event -> {

            final Integer minutes = (Integer) minutesSelector.getValue();
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
        if (Objects.requireNonNull(state.getSessionType()) == SessionType.FIXED) {
            typeSelector.setSelectedItem(FIXED_SESSION);
        }
        else if (state.getSessionType() == SessionType.VARIABLE) {
            typeSelector.setSelectedItem(VARIABLE_SESSION);
        }

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
        // Screw you checkstyle for not allowing return statements
        if (fileOptions != null) {
            // Extract the change listener from the file selector.
            final ActionListener[] fileSelectorChangeListeners = fileSelector.getActionListeners();
            final ActionListener fileSelectorChangeListener;

            if (fileSelectorChangeListeners.length > 0) {
                fileSelectorChangeListener = fileSelectorChangeListeners[0];
            }
            else {
                fileSelectorChangeListener = null;
            }

            // Temporarily remove listner to avoid firing change evnets when updating
            fileSelector.removeActionListener(fileSelectorChangeListener);

            // Repopulate selector
            fileSelector.removeAllItems();
            for (String fileName : fileOptions) {
                fileSelector.addItem(fileName);
            }

            if (fileSelectorChangeListener != null) {
                // Re add the change listener
                fileSelector.addActionListener(fileSelectorChangeListener);
            }

            final String previousSelectedFile = viewModel.getState().getReferenceFile();
            if (previousSelectedFile != null && !previousSelectedFile.isEmpty()
                && fileOptions.contains(previousSelectedFile)) {
                // Select previous selcted file
                fileSelector.setSelectedItem(previousSelectedFile);
            }
            else if (fileSelector.getItemCount() > 0) {
                // set to first item if possible
                fileSelector.setSelectedIndex(0);
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
            final StudySessionConfigState state = (StudySessionConfigState) evt.getNewValue();
            setFields(state);
        }
        else if (evt.getPropertyName().equals("error")) {
            final StudySessionConfigState state = (StudySessionConfigState) evt.getNewValue();
            JOptionPane.showMessageDialog(this, state.getError(), "Configuration Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (evt.getPropertyName().equals("fileOptions")) {
            final List<String> fileOptions = viewModel.getState().getFileOptions();
            updateFileSelector(fileOptions);
        }
    }
}
