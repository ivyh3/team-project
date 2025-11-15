package view;

import app.AppBuilder;
import interface_adapter.controller.StudySessionConfigController;
import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionConfigViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.Map;

public class StudySessionConfigView extends StatefulView<StudySessionConfigState> {
    private final JPanel viewHeader = new ViewHeader("Session Config");
    private final JLabel currentStepLabel = new JLabel("1.Select Type");
    private final JPanel mainCardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel chooseSessionTypePanel, chooseDurationPanel, chooseReferenceMaterialsPanel;
    private StudySessionConfigController studySessionConfigController;
    public StudySessionConfigView(StudySessionConfigViewModel viewModel) {
        super("studySessionConfig", viewModel);


        mainCardPanel.setLayout(cardLayout);
        chooseSessionTypePanel = buildChooseSessionTypePanel();
        chooseDurationPanel = buildChooseDurationPanel();
        chooseReferenceMaterialsPanel = buildChooseReferenceMaterialsPanel();

        mainCardPanel.add(chooseSessionTypePanel, "chooseSessionType");
        mainCardPanel.add(chooseDurationPanel, "chooseDuration");
        mainCardPanel.add(chooseReferenceMaterialsPanel, "chooseReferenceMaterials");

        cardLayout.show(mainCardPanel, "chooseSessionType");

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
        hoursSelector.setMaximumSize(new Dimension(100, 30));
        hoursSelector.setFont(new Font(null, Font.PLAIN, 20));
        JLabel minutesLabel = new JLabel("Minutes");
        minutesLabel.setFont(new Font(null, Font.BOLD, 24));
        JSpinner minutesSelector = new JSpinner(new SpinnerNumberModel(0, 0, 55, 5));
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
            StudySessionConfigState state = viewModel.getState();
            state.setSessionType(null);
            state.setStep(StudySessionConfigState.ConfigStep.CHOOSE_TYPE);
            viewModel.firePropertyChange();
        });
        nextButton.addActionListener(e -> {
            StudySessionConfigState state = viewModel.getState();
            state.setTargetDuration((Integer) hoursSelector.getValue() * 60 + (Integer) minutesSelector.getValue());
            state.setStep(StudySessionConfigState.ConfigStep.CHOOSE_REFERENCE);
            viewModel.firePropertyChange();
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
            StudySessionConfigState state = viewModel.getState();
            state.setSessionType(StudySessionConfigState.SessionType.FIXED);
            state.setStep(StudySessionConfigState.ConfigStep.CHOOSE_DURATION);
            studySessionConfigController.execute(state);
//            viewModel.firePropertyChange();
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
            StudySessionConfigState state = viewModel.getState();
            state.setSessionType(StudySessionConfigState.SessionType.VARIABLE);
            state.setStep(StudySessionConfigState.ConfigStep.CHOOSE_REFERENCE);
            viewModel.firePropertyChange();
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
            if (e.getSource().equals(returnButton)) {
                viewModel.setState(new StudySessionConfigState());
                AppBuilder.viewManagerModel.setView("dashboard");
            }
        });
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        chooseSessionTypePanel.add(buttonContainer);
        chooseSessionTypePanel.add(Box.createVerticalGlue());
        chooseSessionTypePanel.add(returnButton);

        return chooseSessionTypePanel;
    }

    private JPanel buildChooseReferenceMaterialsPanel() {
        JPanel chooseReferenceMaterialsPanel = new JPanel();


        JButton cancelButton = new JButton("Cancel");
        JButton nextButton = new JButton("Next");

        cancelButton.addActionListener(e -> {
            StudySessionConfigState state = viewModel.getState();
            state.setSessionType(null);
            state.setTargetDuration(null);
            state.setStep(StudySessionConfigState.ConfigStep.CHOOSE_TYPE);
            viewModel.firePropertyChange();
        });
        nextButton.addActionListener(e -> {
            StudySessionConfigState state = viewModel.getState();
            if (state.getSessionType() == StudySessionConfigState.SessionType.FIXED) {
                // NEED A NAVIGATION USE CASE AND CONTROLLER LATER
                AppBuilder.viewManagerModel.setView("fixedSession");
            } else {
                AppBuilder.viewManagerModel.setView("variableSession");
            }
        });

        chooseReferenceMaterialsPanel.add(cancelButton);
        chooseReferenceMaterialsPanel.add(nextButton);

        return chooseReferenceMaterialsPanel;
    }

    private void setViewStep(StudySessionConfigState.ConfigStep step) {
        switch (step) {
            case CHOOSE_TYPE:
                cardLayout.show(mainCardPanel, "chooseSessionType");
                currentStepLabel.setText("1. Select Type");
                break;
            case CHOOSE_REFERENCE:
                cardLayout.show(mainCardPanel, "chooseReferenceMaterials");
                currentStepLabel.setText("2. Select Reference");
                break;
            case CHOOSE_DURATION:
                cardLayout.show(mainCardPanel, "chooseDuration");
                currentStepLabel.setText("1.5 Select Duration");
                break;
        }
    }

    public void setStudySessionConfigController(StudySessionConfigController studySessionConfigController) {
        this.studySessionConfigController = studySessionConfigController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            StudySessionConfigState state = viewModel.getState();
            System.out.println(state);
            setViewStep(state.getStep());
        }

    }
}
