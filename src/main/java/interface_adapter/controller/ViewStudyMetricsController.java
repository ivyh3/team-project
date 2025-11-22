package interface_adapter.controller;

import use_case.view_study_metrics.ViewStudyMetricsInputBoundary;
import use_case.view_study_metrics.ViewStudyMetricsInputData;
import entity.User;
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
     * @param user the user
     * @param courseId the course ID, or "all" for all courses
     * @param week the time filter (e.g., "week", "month", "all")
     */
    public void execute(User user, String courseId, LocalDateTime week) {
        ViewStudyMetricsInputData inputData = new ViewStudyMetricsInputData(
            user, courseId, week
        );
        interactor.execute(inputData);
    }
}

