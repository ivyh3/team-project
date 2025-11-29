package use_case.view_study_metrics;
import java.time.LocalDateTime;
/**
 * Input data for the View Study Metrics use case.
 */
public class ViewStudyMetricsInputData {
    private final String userId;
    private final LocalDateTime week;
    
    public ViewStudyMetricsInputData(String userId, LocalDateTime week) {
        this.userId = userId;
        this.week = week;
    }
    
    public String getUser() {
        return userId;
    }

    public LocalDateTime getWeek() {
        return week;
    }
}

