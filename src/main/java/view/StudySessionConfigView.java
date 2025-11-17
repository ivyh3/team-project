package view;

import app.AppBuilder;
import interface_adapter.controller.StudySessionConfigController;
import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionConfigViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;

// TODO: Reset stateful components to initial state somehow when resetting state
public class StudySessionConfigView extends StatefulView<StudySessionConfigState> {
    private final JPanel viewHeader = new ViewHeader("Session Config");
    private final JLabel currentStepLabel = new JLabel();
    private final JPanel mainCardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel chooseSessionTypePanel, chooseDurationPanel, chooseReferenceMaterialsPanel;

    private final JComboBox<String> fileSelector = new JComboBox<>();

    private static final Map<StudySessionConfigState.ConfigStep, String> STEP_LABELS = Map.of(
            StudySessionConfigState.ConfigStep.CHOOSE_TYPE, "Select Session Type",
            StudySessionConfigState.ConfigStep.CHOOSE_DURATION, "Select Session Duration",
            StudySessionConfigState.ConfigStep.CHOOSE_REFERENCE, "Provide Session Context"
    );
    private static final Map<StudySessionConfigState.ConfigStep, String> VIEW_NAMES = Map.of(
            StudySessionConfigState.ConfigStep.CHOOSE_TYPE, "chooseSessionType",
            StudySessionConfigState.ConfigStep.CHOOSE_DURATION, "chooseDuration",
            StudySessionConfigState.ConfigStep.CHOOSE_REFERENCE, "chooseReferenceMaterials"
    );

    private StudySessionConfigController studySessionConfigController;

    public StudySessionConfigView(StudySessionConfigViewModel viewModel) {
        super("studySessionConfig", viewModel);


        mainCardPanel.setLayout(cardLayout);
        chooseSessionTypePanel = buildChooseSessionTypePanel();
        chooseDurationPanel = buildChooseDurationPanel();
        chooseReferenceMaterialsPanel = buildChooseReferenceMaterialsPanel();

        mainCardPanel.add(chooseSessionTypePanel, VIEW_NAMES.get(StudySessionConfigState.ConfigStep.CHOOSE_TYPE));
        mainCardPanel.add(chooseDurationPanel, VIEW_NAMES.get(StudySessionConfigState.ConfigStep.CHOOSE_DURATION));
        mainCardPanel.add(chooseReferenceMaterialsPanel, VIEW_NAMES.get(StudySessionConfigState.ConfigStep.CHOOSE_REFERENCE));


        updateFileSelector(viewModel.getState().getFileOptions());
        updateViewStep(viewModel.getState().getStep());

        currentStepLabel.setFont(new Font(null, Font.BOLD, 16));
        viewHeader.add(currentStepLabel, BorderLayout.EAST);
        this.add(viewHeader, BorderLayout.NORTH);
        this.add(mainCardPanel, BorderLayout.CENTER);
    }

    private JPanel buildChooseDurationPanel() {
        JPanel chooseDurationPanel = new JPanel();

        chooseDurationPanel.setLayout(new BoxLayout(chooseDurationPanel, BoxLayout.Y_AXIS));

        JPanel durationSelectionContainer = new JPanel();
        durationSelectionContainer.setLayout(new BoxLayout(durationSelectionContainer, BoxLayout.X_AXIS));

        JLabel hoursLabel = new JLabel("Hours");
        hoursLabel.setFont(new Font(null, Font.BOLD, 24));
        JSpinner hoursSelector = new JSpinner(new SpinnerNumberModel(1, 0, 23, 1));
        hoursSelector.setEditor(new JSpinner.DefaultEditor(hoursSelector)); // Prevent manual edit of field
//        ((JSpinner.DefaultEditor) hoursSelector.getEditor()).getTextField().setEditable(false); alternative way (right aligned text)

        hoursSelector.setMaximumSize(new Dimension(100, 30));
        hoursSelector.setFont(new Font(null, Font.PLAIN, 20));

        JLabel minutesLabel = new JLabel("Minutes");
        minutesLabel.setFont(new Font(null, Font.BOLD, 24));
        JSpinner minutesSelector = new JSpinner(new SpinnerNumberModel(0, 0, 55, 5));
        minutesSelector.setEditor(new JSpinner.DefaultEditor(minutesSelector)); // Prevent manual edit of field
        minutesSelector.setMaximumSize(new Dimension(100, 30));
        minutesSelector.setFont(new Font(null, Font.PLAIN, 20));


        durationSelectionContainer.add(hoursLabel);
        durationSelectionContainer.add(Box.createRigidArea(new Dimension(20, 0)));
        durationSelectionContainer.add(hoursSelector);
        durationSelectionContainer.add(Box.createRigidArea(new Dimension(80, 0)));
        durationSelectionContainer.add(minutesLabel);
        durationSelectionContainer.add(Box.createRigidArea(new Dimension(20, 0)));
        durationSelectionContainer.add(minutesSelector);

        JPanel navigationContainer = new JPanel();
        navigationContainer.setLayout(new BoxLayout(navigationContainer, BoxLayout.X_AXIS));
        JButton cancelButton = new JButton("Cancel");
        JButton nextButton = new JButton("Next");

        cancelButton.addActionListener(e -> {
            StudySessionConfigState state = viewModel.getState().copy();
            studySessionConfigController.undo(state);
        });
        nextButton.addActionListener(e -> {
            StudySessionConfigState state = viewModel.getState().copy();
            state.setTargetDuration((Integer) hoursSelector.getValue() * 60 + (Integer) minutesSelector.getValue());
            studySessionConfigController.execute(state);
        });

        navigationContainer.add(cancelButton);
        navigationContainer.add(Box.createRigidArea(new Dimension(40, 0)));
        navigationContainer.add(nextButton);

        chooseDurationPanel.add(Box.createVerticalGlue());
        chooseDurationPanel.add(durationSelectionContainer);
        chooseDurationPanel.add(Box.createVerticalGlue());
        chooseDurationPanel.add(navigationContainer);

        return chooseDurationPanel;
    }

    private JPanel buildChooseSessionTypePanel() {


        JPanel chooseSessionTypePanel = new JPanel();
        chooseSessionTypePanel.setLayout(new BoxLayout(chooseSessionTypePanel, BoxLayout.Y_AXIS));


        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));

        JPanel timedSessionContainer = new JPanel();
        timedSessionContainer.setLayout(new BoxLayout(timedSessionContainer, BoxLayout.Y_AXIS));
        JPanel variableSessionContainer = new JPanel();
        variableSessionContainer.setLayout(new BoxLayout(variableSessionContainer, BoxLayout.Y_AXIS));


        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(timedSessionContainer);
        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(variableSessionContainer);
        buttonContainer.add(Box.createHorizontalGlue());


        JLabel timedSessionHeading = new JLabel("Timed Session");
        timedSessionHeading.setFont(new Font(null, Font.BOLD, 28));
        JLabel timedSessionLabel = new JLabel("Allocate a specified focus time for studying.");
        timedSessionLabel.setFont(new Font(null, Font.ITALIC, 16));
        JButton timedSessionButton = new JButton("I want a timed session!");

        timedSessionButton.addActionListener(e -> {
            StudySessionConfigState state = viewModel.getState().copy();
            state.setSessionType(StudySessionConfigState.SessionType.FIXED);
            studySessionConfigController.execute(state);
        });

        timedSessionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        timedSessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timedSessionHeading.setAlignmentX(Component.CENTER_ALIGNMENT);


        timedSessionContainer.add(Box.createVerticalGlue());
        timedSessionContainer.add(timedSessionHeading);
        timedSessionContainer.add(timedSessionLabel);
        timedSessionContainer.add(Box.createVerticalGlue());
        timedSessionContainer.add(timedSessionButton);

        JLabel variableSessionHeading = new JLabel("Variable Session");
        variableSessionHeading.setFont(new Font(null, Font.BOLD, 28));
        JLabel variableSessionLabel = new JLabel("Study until you're ready.");
        variableSessionLabel.setFont(new Font(null, Font.ITALIC, 16));


        JButton variableSessionButton = new JButton("I want a variable session!");
        variableSessionButton.addActionListener(e -> {
            StudySessionConfigState state = viewModel.getState().copy();
            state.setSessionType(StudySessionConfigState.SessionType.VARIABLE);
            studySessionConfigController.execute(state);
        });

        variableSessionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        variableSessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        variableSessionHeading.setAlignmentX(Component.CENTER_ALIGNMENT);


        variableSessionContainer.add(Box.createVerticalGlue());
        variableSessionContainer.add(variableSessionHeading);
        variableSessionContainer.add(variableSessionLabel);
        variableSessionContainer.add(Box.createVerticalGlue());
        variableSessionContainer.add(variableSessionButton);


        final JButton returnButton = new JButton("Cancel");
        returnButton.addActionListener(e -> {
            StudySessionConfigState state = viewModel.getState().copy();
            studySessionConfigController.undo(state);
        });
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        chooseSessionTypePanel.add(buttonContainer);
        chooseSessionTypePanel.add(Box.createVerticalGlue());
        chooseSessionTypePanel.add(returnButton);

        return chooseSessionTypePanel;
    }

    private JPanel buildChooseReferenceMaterialsPanel() {
        // TODO: Make this look better
        JPanel chooseReferenceMaterialsPanel = new JPanel();

        JPanel promptPanel = new JPanel();
        promptPanel.setBorder(BorderFactory.createTitledBorder("Study Session Context"));
        JTextArea promptTextArea = new JTextArea();
        promptPanel.add(promptTextArea);


        JPanel selectorPanel = new JPanel();
        selectorPanel.setBorder(BorderFactory.createTitledBorder("Study Session Textbook"));
        selectorPanel.add(fileSelector);


        JButton cancelButton = new JButton("Cancel");
        JButton nextButton = new JButton("Next");

        cancelButton.addActionListener(e -> {
            StudySessionConfigState state = viewModel.getState().copy();
            studySessionConfigController.undo(state);
        });
        nextButton.addActionListener(e -> {
            StudySessionConfigState state = viewModel.getState().copy();
            state.setPrompt(promptTextArea.getText());
            state.setReferenceFile((String) fileSelector.getSelectedItem());
            studySessionConfigController.execute(state);
        });

        chooseReferenceMaterialsPanel.add(promptPanel);
        chooseReferenceMaterialsPanel.add(selectorPanel);
        chooseReferenceMaterialsPanel.add(cancelButton);
        chooseReferenceMaterialsPanel.add(nextButton);

        return chooseReferenceMaterialsPanel;
    }

    private void updateFileSelector(List<String> fileNames) {
        fileSelector.removeAllItems();
        if (fileNames == null) return;
        for (String fileName : fileNames) {
            fileSelector.addItem(fileName);
        }
    }

    private void updateViewStep(StudySessionConfigState.ConfigStep step) {
        cardLayout.show(mainCardPanel, VIEW_NAMES.get(step));
        currentStepLabel.setText(STEP_LABELS.get(step));
    }

    public void setStudySessionConfigController(StudySessionConfigController studySessionConfigController) {
        this.studySessionConfigController = studySessionConfigController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            StudySessionConfigState state = (StudySessionConfigState) evt.getNewValue();
            System.out.println(state);
            updateViewStep(state.getStep());
            updateFileSelector(state.getFileOptions());
        }
        else if (evt.getPropertyName().equals("errors")) {
            StudySessionConfigState state = (StudySessionConfigState) evt.getNewValue();
            List<String> errors = state.getErrors();
            if (errors != null && !errors.isEmpty()) {
                String errorMessage = String.join("\n", errors);
                JOptionPane.showMessageDialog(this, errorMessage, "Configuration Errors", JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}
