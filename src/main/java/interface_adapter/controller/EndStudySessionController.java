package interface_adapter.controller;

import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.StudySessionState;
import use_case.end_study_session.EndStudySessionInputBoundary;
import use_case.end_study_session.EndStudySessionInputData;

/**
 * Controller for ending a study session.
 */
public class EndStudySessionController {
    private final EndStudySessionInputBoundary interactor;

    public EndStudySessionController(EndStudySessionInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Ends the study session.
     *
     * @param userId the user Id
     * @param state The current state of the study session
     */
    public void execute(String userId, StudySessionState state) {
        final EndStudySessionInputData inputData =
            new EndStudySessionInputData(userId, state);
        interactor.execute(inputData);

    }
}