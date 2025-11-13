package interface_adapter.controller;

import use_case.view_study_metrics.ViewStudyMetricsInputBoundary;
import use_case.view_study_metrics.ViewStudyMetricsInputData;

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
     * @param userId the user ID
     * @param courseId the course ID (optional, can be null for all courses)
     * @param timeFilter the time filter (e.g., "week", "month", "all")
     */
    public void execute(String userId, String courseId, String timeFilter) {
        ViewStudyMetricsInputData inputData = new ViewStudyMetricsInputData(
            userId, courseId, timeFilter
        );
        interactor.execute(inputData);
    }
}

