package use_case.start_study_session;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Input data for the Start Study Session use case.
 */
public class StartStudySessionInputData {
    private final String userId;
    private final String courseId;
    private final LocalDateTime startTime;
    private final List<String> referenceMaterialIds;
    private final String prompt;
    
    public StartStudySessionInputData(String userId, String courseId, LocalDateTime startTime, 
                                     List<String> referenceMaterialIds, String prompt) {
        this.userId = userId;
        this.courseId = courseId;
        this.startTime = startTime;
        this.referenceMaterialIds = referenceMaterialIds;
        this.prompt = prompt;
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
    
    public List<String> getReferenceMaterialIds() {
        return referenceMaterialIds;
    }
    
    public String getPrompt() {
        return prompt;
    }
}

