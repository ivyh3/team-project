package use_case.view_study_metrics;

/**
 * Input data for the View Study Metrics use case.
 */
public class ViewStudyMetricsInputData {
    private final String userId;
    private final String courseId;
    private final String timeFilter;
    
    public ViewStudyMetricsInputData(String userId, String courseId, String timeFilter) {
        this.userId = userId;
        this.courseId = courseId;
        this.timeFilter = timeFilter;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public String getTimeFilter() {
        return timeFilter;
    }
}

