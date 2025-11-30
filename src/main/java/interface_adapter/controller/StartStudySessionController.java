package interface_adapter.controller;

import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.StudySessionConfigState;
import use_case.start_study_session.StartStudySessionInputBoundary;
import use_case.start_study_session.StartStudySessionInputData;

/**
 * Controller for starting a study session.
 */
public class StartStudySessionController {
    private final DashboardViewModel dashboardViewModel;
    private final StartStudySessionInputBoundary interactor;

    public StartStudySessionController(StartStudySessionInputBoundary interactor,
                                       DashboardViewModel dashboardViewModel) {
        this.interactor = interactor;
        this.dashboardViewModel = dashboardViewModel;
    }

    /**
     * Attempts to start a study session with the current config.
     *
     * @param state The current configuration
     */
    public void execute(StudySessionConfigState state) {
        final StartStudySessionInputData inputData = new StartStudySessionInputData(
            dashboardViewModel.getState().getUserId(),
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
        interactor.refreshFileOptions(dashboardViewModel.getState().getUserId());
    }
}
