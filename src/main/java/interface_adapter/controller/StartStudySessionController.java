package interface_adapter.controller;

import interface_adapter.view_model.StudySessionConfigState;
import use_case.start_study_session.StartStudySessionInputBoundary;
import use_case.start_study_session.StartStudySessionInputData;

/**
 * Controller for starting a study session.
 */
public class StartStudySessionController {
    private final StartStudySessionInputBoundary interactor;

    public StartStudySessionController(StartStudySessionInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Attempts to start a study session with the current config.
     *
     * @param state The current configuration
     */
    public void execute(StudySessionConfigState state) {
        final StartStudySessionInputData inputData = new StartStudySessionInputData(
            state.copy());
        interactor.execute(inputData);
    }

    /**
     * Abort configuration and return.
     */
    public void abortStudySessionConfig() {
        interactor.abortStudySessionConfig();
    }

    /**
     * Refresh the file options that are available.
     */
    public void refreshFileOptions() {
        interactor.refreshFileOptions();
    }
}
