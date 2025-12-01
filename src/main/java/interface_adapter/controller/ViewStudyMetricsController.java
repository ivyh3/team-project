package interface_adapter.controller;

import use_case.view_study_metrics.ViewStudyMetricsInputBoundary;
import use_case.view_study_metrics.ViewStudyMetricsInputData;
import java.time.LocalDateTime;

/**
 * Controller for the View Study Metrics use case.
 */
public class ViewStudyMetricsController {
    private final ViewStudyMetricsInputBoundary interactor;

    public ViewStudyMetricsController(ViewStudyMetricsInputBoundary interactor) {
        this.interactor = interactor;
    }
    
    /**
     * Executes the view study metrics use case.
     * @param userId the user id
     * @param week the Sunday expected
     */
    public void execute(String userId, LocalDateTime week) {
        final ViewStudyMetricsInputData inputData = new ViewStudyMetricsInputData(userId, week);
        interactor.execute(inputData);
    }
}

