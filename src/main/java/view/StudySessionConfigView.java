package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Objects;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.controller.StartStudySessionController;
import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionConfigState.SessionType;
import interface_adapter.view_model.StudySessionConfigViewModel;

public class StudySessionConfigView extends StatefulView<StudySessionConfigState> {
    public static final String VARIABLE_SESSION = "Variable Session";
    public static final String FIXED_SESSION = "Timed Session";
    private static final String VARIABLE_SESSION_DESCRIPTION = "Study for as long as you want until you're ready.";
    private static final String FIXED_SESSION_DESCRIPTION = "Specify a quantity of studying time.";
    private static final int TEXT_LG = 20;
    private static final int TEXT_BASE = 16;
    private static final int TEXT_SM = 12;
    private static final int TIME_SELECTOR_WIDTH = 100;
    private static final int TEXT_SELECTOR_HEIGHT = 30;
    private static final int MAX_MINS = 55;
    private static final int MINS_STEP = 5;
    private static final int HOUR_STEP = 1;
    private static final int MAX_HOUR = 23;
    private static final int GAP_SM = 10;
    private static final int GAP_LG = 40;

    private final JPanel selectDurationPanel;
    private final JSpinner hoursSelector = new JSpinner();
    private final JSpinner minutesSelector = new JSpinner();
    private final JTextArea promptArea = new JTextArea(4, 40);
    private final JComboBox<String> typeSelector = new JComboBox<>();
    private final JComboBox<String> fileSelector = new JComboBox<>();
    private final DashboardViewModel dashboardViewModel;
    private final StartStudySessionController startStudySessionController;

    public StudySessionConfigView(StudySessionConfigViewModel viewModel,
                                  DashboardViewModel dashboardViewModel,
                                  StartStudySessionController controller) {
        super("studySessionConfig", viewModel);
        this.dashboardViewModel = dashboardViewModel;
        this.startStudySessionController = controller;

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

        cancelButton.addActionListener(event -> {
            if (startStudySessionController != null) {
                startStudySessionController.abortStudySessionConfig();
            }
        });
        nextButton.addActionListener(event -> {
            if (startStudySessionController != null) {
                final StudySessionConfigState currentConfig = viewModel.getState().copy();
                startStudySessionController.execute(
                        this.dashboardViewModel.getState().getUserId(), currentConfig);
            }
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
            if (startStudySessionController != null) {
                startStudySessionController.refreshFileOptions(
                        this.dashboardViewModel.getState().getUserId());
            }
        });
        viewHeader.add(refreshButton, BorderLayout.EAST);

        updateDurationPanelVisibility(getSafeSessionType(viewModel.getState()));
        updateFileSelector(viewModel.getState().getFileOptions());
        attachListeners();
        setFields(viewModel.getState());

        this.add(viewHeader, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
    }

    private SessionType getSafeSessionType(StudySessionConfigState state) {
        return state.getSessionType() != null ? state.getSessionType() : SessionType.VARIABLE;
    }

    private static SessionType sessionStringToSessionType(String choice) {
        return VARIABLE_SESSION.equals(choice) ? SessionType.VARIABLE : SessionType.FIXED;
    }

    private void updateDurationPanelVisibility(SessionType sessionType) {
        selectDurationPanel.setVisible(sessionType == SessionType.FIXED);
    }

    private JPanel buildChooseDurationPanel() {
        JPanel chooseDurationPanel = new JPanel();
        chooseDurationPanel.setLayout(new GridLayout(1, 2));
        chooseDurationPanel.setBorder(BorderFactory.createTitledBorder("Session Duration"));

        // Hours
        JPanel hoursContainer = new JPanel();
        hoursContainer.setLayout(new BoxLayout(hoursContainer, BoxLayout.X_AXIS));
        JLabel hoursLabel = new JLabel("Hours");
        hoursLabel.setFont(new Font(null, Font.BOLD, TEXT_LG));

        hoursSelector.setModel(new SpinnerNumberModel(0, 0, MAX_HOUR, HOUR_STEP));
        hoursSelector.setEditor(new JSpinner.DefaultEditor(hoursSelector));
        hoursSelector.setMaximumSize(new Dimension(TIME_SELECTOR_WIDTH, TEXT_SELECTOR_HEIGHT));
        hoursSelector.setFont(new Font(null, Font.PLAIN, TEXT_LG));

        hoursContainer.add(hoursLabel);
        hoursContainer.add(Box.createRigidArea(new Dimension(GAP_SM, 0)));
        hoursContainer.add(hoursSelector);

        // Minutes
        JPanel minutesContainer = new JPanel();
        minutesContainer.setLayout(new BoxLayout(minutesContainer, BoxLayout.X_AXIS));
        JLabel minutesLabel = new JLabel("Minutes");
        minutesLabel.setFont(new Font(null, Font.BOLD, TEXT_LG));

        minutesSelector.setModel(new SpinnerNumberModel(0, 0, MAX_MINS, MINS_STEP));
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
        JPanel chooseSessionTypePanel = new JPanel();
        chooseSessionTypePanel.setBorder(BorderFactory.createTitledBorder("Session Type"));
        chooseSessionTypePanel.setLayout(new GridLayout(1, 2));

        typeSelector.addItem(VARIABLE_SESSION);
        typeSelector.addItem(FIXED_SESSION);
        typeSelector.setSelectedIndex(0);
        typeSelector.setFont(new Font(null, Font.PLAIN, TEXT_LG));
        chooseSessionTypePanel.add(typeSelector);

        return chooseSessionTypePanel;
    }

    private JPanel buildChooseReferencePanel() {
        JPanel chooseReferenceMaterialsPanel = new JPanel();
        chooseReferenceMaterialsPanel.setLayout(new BoxLayout(chooseReferenceMaterialsPanel, BoxLayout.X_AXIS));
        chooseReferenceMaterialsPanel.setBorder(BorderFactory.createTitledBorder("Study Session Context"));

        promptArea.setBorder(BorderFactory.createTitledBorder("What are you studying?"));
        chooseReferenceMaterialsPanel.add(promptArea);

        JPanel selectorPanel = new JPanel();
        selectorPanel.setBorder(BorderFactory.createTitledBorder("Textbook"));
        selectorPanel.add(fileSelector);
        chooseReferenceMaterialsPanel.add(selectorPanel);

        return chooseReferenceMaterialsPanel;
    }

    private void attachListeners() {
        typeSelector.addActionListener(event -> {
            String choice = (String) typeSelector.getSelectedItem();
            if (choice != null) {
                SessionType sessionType = sessionStringToSessionType(choice);
                viewModel.getState().setSessionType(sessionType);
                updateDurationPanelVisibility(sessionType);
                viewModel.firePropertyChange();
            }
        });

        fileSelector.addActionListener(event -> {
            String choice = (String) fileSelector.getSelectedItem();
            if (choice != null) {
                viewModel.getState().setReferenceFile(choice);
            }
        });

        hoursSelector.addChangeListener(event -> viewModel.getState().setTargetDurationHours((Integer) hoursSelector.getValue()));
        minutesSelector.addChangeListener(event -> viewModel.getState().setTargetDurationMinutes((Integer) minutesSelector.getValue()));

        promptArea.getDocument().addDocumentListener(new DocumentListener() {
            private void updatePrompt() {
                viewModel.getState().setPrompt(promptArea.getText());
            }
            @Override public void insertUpdate(DocumentEvent e) { updatePrompt(); }
            @Override public void removeUpdate(DocumentEvent e) { updatePrompt(); }
            @Override public void changedUpdate(DocumentEvent e) { updatePrompt(); }
        });
    }

    private void setFields(StudySessionConfigState state) {
        SessionType sessionType = getSafeSessionType(state);
        typeSelector.setSelectedItem(sessionType == SessionType.FIXED ? FIXED_SESSION : VARIABLE_SESSION);

        hoursSelector.setValue(state.getTargetDurationHours());
        minutesSelector.setValue(state.getTargetDurationMinutes());
        promptArea.setText(state.getPrompt());

        updateFileSelector(state.getFileOptions());
        if (state.getReferenceFile() != null) {
            fileSelector.setSelectedItem(state.getReferenceFile());
        }

        updateDurationPanelVisibility(sessionType);
    }

    private void updateFileSelector(List<String> fileOptions) {
        if (fileOptions != null) {
            ActionListener[] listeners = fileSelector.getActionListeners();
            for (ActionListener l : listeners) fileSelector.removeActionListener(l);

            fileSelector.removeAllItems();
            for (String f : fileOptions) fileSelector.addItem(f);

            for (ActionListener l : listeners) fileSelector.addActionListener(l);

            String previous = viewModel.getState().getReferenceFile();
            if (previous != null && !previous.isEmpty() && fileOptions.contains(previous)) {
                fileSelector.setSelectedItem(previous);
            } else if (fileSelector.getItemCount() > 0) {
                fileSelector.setSelectedIndex(0);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            setFields((StudySessionConfigState) evt.getNewValue());
        } else if ("error".equals(evt.getPropertyName())) {
            StudySessionConfigState state = (StudySessionConfigState) evt.getNewValue();
            JOptionPane.showMessageDialog(this, state.getError(), "Configuration Error", JOptionPane.ERROR_MESSAGE);
        } else if ("fileOptions".equals(evt.getPropertyName())) {
            updateFileSelector(viewModel.getState().getFileOptions());
        }
    }
}