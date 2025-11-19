package use_case.start_study_session;

import java.time.LocalDateTime;

/**
 * Output data for the Start Study Session use case.
 */
public class StartStudySessionOutputData {
    private final String sessionId;
    private final LocalDateTime startTime;
    private final String courseId;
    
    public StartStudySessionOutputData(String sessionId, LocalDateTime startTime, String courseId) {
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.courseId = courseId;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public String getCourseId() {
        return courseId;
    }
}

