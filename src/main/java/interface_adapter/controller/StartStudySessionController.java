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
     * Start a study session with the given configuration.
     *
     * @param userId the ID of the user
     * @param state the current configuration state
     */
    public void execute(String userId, StudySessionConfigState state) {
        if (userId == null || userId.isBlank() || state == null) return;

        StartStudySessionInputData inputData = new StartStudySessionInputData(userId, state.copy());
        interactor.execute(inputData);
    }

    /**
     * Abort the current study session configuration.
     */
    public void abortStudySessionConfig() {
        interactor.abortStudySessionConfig();
    }

    /**
     * Refresh the available file options for the user.
     *
     * @param userId the ID of the user
     */
    public void refreshFileOptions(String userId) {
        if (userId == null || userId.isBlank()) return;
        interactor.refreshFileOptions(userId);
    }
}