package use_case.schedule_study_session;

import java.time.LocalDateTime;

/**
 * Input data for the Schedule Study Session use case.
 */
public class ScheduleStudySessionInputData {
    private final String userId;
    private final String courseId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final boolean syncWithCalendar;
    private final String title;
    
    public ScheduleStudySessionInputData(String userId, String courseId, 
                                        LocalDateTime startTime, LocalDateTime endTime,
                                        boolean syncWithCalendar, String title) {
        this.userId = userId;
        this.courseId = courseId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.syncWithCalendar = syncWithCalendar;
        this.title = title;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public boolean isSyncWithCalendar() {
        return syncWithCalendar;
    }

    public String getTitle() { return title; }
}

