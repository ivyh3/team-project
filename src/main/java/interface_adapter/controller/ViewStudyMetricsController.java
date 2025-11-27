package interface_adapter.controller;

import interface_adapter.view_model.DashboardViewModel;
import use_case.view_study_metrics.ViewStudyMetricsInputBoundary;
import use_case.view_study_metrics.ViewStudyMetricsInputData;
import java.time.LocalDateTime;

/**
 * Controller for the View Study Metrics use case.
 */
public class ViewStudyMetricsController {
    private final ViewStudyMetricsInputBoundary interactor;
    private final DashboardViewModel dashboardViewModel;

    public ViewStudyMetricsController(ViewStudyMetricsInputBoundary interactor, DashboardViewModel dashboardViewModel) {
        this.interactor = interactor;
        this.dashboardViewModel = dashboardViewModel;
    }
    
    /**
     * Executes the view study metrics use case.
//     * @param userId the user
     * @param week the time filter (e.g., "week", "month", "all")
     */
    public void execute(LocalDateTime week) { // TODO: note: userid taken out bc i guess we should get the id here?
        String userId = dashboardViewModel.getState().getUserId();
        System.out.println("User: " + userId);
        ViewStudyMetricsInputData inputData = new ViewStudyMetricsInputData(userId, week);
        interactor.execute(inputData);
    }
}

