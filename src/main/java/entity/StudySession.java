package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a study session.
 */
public class StudySession {
    private String id;
    private String ownerUid;
    private String courseId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;
    private List<String> referenceMaterialIds;
    private String quizId;
    private String status;
    private String calendarEventId;
    private boolean syncCalendar;
    
    public StudySession(String id, String ownerUid, String courseId, LocalDateTime startTime) {
        this.id = id;
        this.ownerUid = ownerUid;
        this.courseId = courseId;
        this.startTime = startTime;
        this.status = "active";
        this.syncCalendar = false;
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getOwnerUid() {
        return ownerUid;
    }
    
    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        if (startTime != null && endTime != null) {
            this.duration = Duration.between(startTime, endTime);
        }
    }
    
    public Duration getDuration() {
        return duration;
    }
    
    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    
    public List<String> getReferenceMaterialIds() {
        return referenceMaterialIds;
    }
    
    public void setReferenceMaterialIds(List<String> referenceMaterialIds) {
        this.referenceMaterialIds = referenceMaterialIds;
    }
    
    public String getQuizId() {
        return quizId;
    }
    
    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCalendarEventId() {
        return calendarEventId;
    }
    
    public void setCalendarEventId(String calendarEventId) {
        this.calendarEventId = calendarEventId;
    }
    
    public boolean isSyncCalendar() {
        return syncCalendar;
    }
    
    public void setSyncCalendar(boolean syncCalendar) {
        this.syncCalendar = syncCalendar;
    }
}

