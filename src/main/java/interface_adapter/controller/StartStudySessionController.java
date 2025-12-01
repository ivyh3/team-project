package interface_adapter.controller;

import interface_adapter.view_model.DashboardViewModel;
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
     * @param userId the current state
     * @param state The current configuration
     */
    public void execute(String userId, StudySessionConfigState state) {
        final StartStudySessionInputData inputData = new StartStudySessionInputData(
            userId,
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
     *
     * @param userId The User ID
     */
    public void refreshFileOptions(String userId) {
        interactor.refreshFileOptions(userId);
    }
}
