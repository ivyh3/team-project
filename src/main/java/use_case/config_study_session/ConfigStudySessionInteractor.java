package use_case.config_study_session;

import interface_adapter.view_model.StudySessionConfigState;

import java.time.LocalDateTime;

public class ConfigStudySessionInteractor implements ConfigStudySessionInputBoundary {
    private final ConfigStudySessionOutputBoundary presenter;

    public ConfigStudySessionInteractor(ConfigStudySessionOutputBoundary presenter) {
        this.presenter = presenter;
    }

    public void execute(StudySessionConfigState state) {
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
        switch (state.getSessionType()) {
            case FIXED:
                state.setStep(StudySessionConfigState.ConfigStep.CHOOSE_DURATION);
                break;
            case VARIABLE:
                state.setStep(StudySessionConfigState.ConfigStep.CHOOSE_REFERENCE);
                break;
        }
        presenter.updateConfig(state);
    }

    private void handleDurationSelection(StudySessionConfigState state) {
        if (state.getTargetDuration() == null || state.getTargetDuration() <= 0) {
            // Throw exception
        }
        state.setStep(StudySessionConfigState.ConfigStep.CHOOSE_REFERENCE);
        presenter.updateConfig(state);
    }

    private void handleReferenceSelection(StudySessionConfigState state) {
        String file = state.getReferenceFile();
        if (file == null) {
            // Throw exception
        } else if (!checkIfFileExists(file)) {
            // throw exception
        }

        String prompt = state.getPrompt();

        if (prompt == null || prompt.isEmpty()) {
            // throw exception
        }

        // Valid reference materials (textbook, prompt)
        presenter.startStudySession(state);
    }

    private boolean checkIfFileExists(String file) {
        return true; // Temporary.
    }
}
