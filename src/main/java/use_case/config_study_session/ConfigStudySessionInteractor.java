package use_case.config_study_session;

import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionConfigState.ConfigStep;
import interface_adapter.view_model.StudySessionConfigState.SessionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigStudySessionInteractor implements ConfigStudySessionInputBoundary {
    private final ConfigStudySessionOutputBoundary presenter;
    // private ReferenceFilesDataAccessInterface referenceFilesDataAccessObject
    private final List<String> files = Arrays.asList("mat223.pdf", "longer_textbook_name_adfasdf.pdf", "csc222.pdf",
            "pdf.pdf");

    public ConfigStudySessionInteractor(ConfigStudySessionOutputBoundary presenter) {
        this.presenter = presenter;
    }

    public void execute(ConfigStudySessionInputData inputData) {
        // TODO: Remove.
        StudySessionConfigState state = inputData.getState();
        switch (state.getStep()) {
            case CHOOSE_TYPE:
                handleSessionTypeSelection(state);
                break;
            case CHOOSE_DURATION:
                handleDurationSelection(state);
                break;
            case CHOOSE_REFERENCE:
                handleReferenceSelection(state);
                break;
        }
    }

    private void handleSessionTypeSelection(StudySessionConfigState state) {
        StudySessionConfigState nextState = state;

        switch (nextState.getSessionType()) {
            case FIXED:
                nextState.setStep(ConfigStep.CHOOSE_DURATION);
                break;
            case VARIABLE:
                nextState.setStep(ConfigStep.CHOOSE_REFERENCE);
                nextState.setFileOptions(files); // prep options
                break;
        }

        ConfigStudySessionOutputData outputData = new ConfigStudySessionOutputData(
            nextState
        );

        presenter.updateConfig(outputData);
    }

    private void handleDurationSelection(StudySessionConfigState state) {
        List<String> errors = new ArrayList<>();
        if (state.getTargetDuration() == null || state.getTargetDuration() <= 0) {
            errors.add("Please study a bit more seriously!");
        }

        if (!errors.isEmpty()) {
            presenter.prepareErrorView(errors);
            return;
        }

        state.setStep(ConfigStep.CHOOSE_REFERENCE);
        state.setFileOptions(files); // prep options

        ConfigStudySessionOutputData outputData = new ConfigStudySessionOutputData(
                state);
        presenter.updateConfig(outputData);
    }

    private void handleReferenceSelection(StudySessionConfigState state) {
        List<String> errors = new ArrayList<>();
        String file = state.getReferenceFile();
        if (file == null || file.isEmpty()) {
            errors.add("Please select a reference file.");
        } else if (!checkIfFileExists(file)) {
            errors.add("Reference file does not exist in storage.");
        }

        String prompt = state.getPrompt();

        if (prompt == null || prompt.isEmpty()) {
            errors.add("Please provide a prompt describing what to study.");
        }

        ConfigStudySessionOutputData outputData = new ConfigStudySessionOutputData(
            state
        );
        if (errors.isEmpty()) {
            // No errors, can start session
            presenter.startStudySession(outputData);
        } else {
            // Errors, remain on current step
            presenter.prepareErrorView(errors);
        }
    }

    private boolean checkIfFileExists(String file) {
        return true; // Temporary.
    }

    @Override
    public void setSessionDuration(ConfigStudySessionInputData inputData) {
        handleDurationSelection(inputData.getState());
    }

    @Override
    public void setSessionType(ConfigStudySessionInputData inputData) {
        handleSessionTypeSelection(inputData.getState());
    }

    @Override
    public void setSessionReferenceMaterials(ConfigStudySessionInputData inputData) {
        handleReferenceSelection(inputData.getState());
    }

    @Override
    public void undo(ConfigStudySessionInputData inputData) {
        StudySessionConfigState nextState = inputData.getState();
        switch (inputData.getState().getStep()) {
            case CHOOSE_TYPE: // Current step is choosing the type.
                // Nothing left to undo, so just abort the config entirely.
                presenter.abortStudySessionConfig();
                nextState = new StudySessionConfigState(); // Empty state.
                break;
            case CHOOSE_DURATION: // Current step is choosing duration.
                // Then undoing would mean the user would choose session type again.
                nextState.setStep(ConfigStep.CHOOSE_TYPE);
                nextState.setSessionType(null);
                break;
            case CHOOSE_REFERENCE: // Current step is choosing references.
                if (inputData.getState().getSessionType() == SessionType.FIXED ) {
                    // If the user chose a fixed session, the last step was selecting duration.
                    nextState.setStep(ConfigStep.CHOOSE_DURATION);
                    nextState.setTargetDuration(null);
                }
                else { // Variable session
                    // If the user chose a variable session, the only thing to undo is choosing the type.
                    nextState.setStep(ConfigStep.CHOOSE_TYPE);
                    nextState.setSessionType(null);
                }
                break;
        }

        ConfigStudySessionOutputData outputData = new ConfigStudySessionOutputData(nextState);
        // TODO: Make presenter output boundary have more specific methods.
        presenter.updateConfig(outputData);
    }
}
