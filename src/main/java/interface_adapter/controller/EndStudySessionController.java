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
    private final DashboardViewModel dashboardViewModel;

    public EndStudySessionController(EndStudySessionInputBoundary interactor, DashboardViewModel dashboardViewModel) {
        this.interactor = interactor;
        this.dashboardViewModel = dashboardViewModel;
    }

    /**
     * Ends the study session.
     *
     * @param state The current state of the study session
     */
    public void execute(StudySessionState state) {
        final EndStudySessionInputData inputData =
            new EndStudySessionInputData(dashboardViewModel.getState().getUserId(), state);
        interactor.execute(inputData);

    }
}
