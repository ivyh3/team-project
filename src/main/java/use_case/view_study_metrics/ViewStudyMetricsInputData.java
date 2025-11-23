package use_case.view_study_metrics;
import java.time.LocalDateTime;
import entity.User;
/**
 * Input data for the View Study Metrics use case.
 */
public class ViewStudyMetricsInputData {
    private final User user;
    private final String courseId; //TODO: use for filtering courses or remove?
    private final LocalDateTime week;
    
    public ViewStudyMetricsInputData(User user, String courseId, LocalDateTime week) {
        this.user = user;
        this.courseId = courseId; // "all" should be an option to see averages across all courses
        this.week = week;
    }
    
    public User getUser() {
        return user;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public LocalDateTime getWeek() {
        return week;
    }
}

