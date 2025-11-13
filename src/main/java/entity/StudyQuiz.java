package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a quiz taken after a study session.
 */
public class StudyQuiz {
    private String id;
    private String ownerUid;
    private String studySessionId;
    private List<Question> questions;
    private String courseId;
    private float score;
    private String prompt;
    private List<String> referenceMaterialIds;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Duration duration;
    
    public StudyQuiz(String id, String ownerUid, String studySessionId, 
                    List<Question> questions, String courseId, String prompt, 
                    List<String> referenceMaterialIds) {
        this.id = id;
        this.ownerUid = ownerUid;
        this.studySessionId = studySessionId;
        this.questions = questions;
        this.courseId = courseId;
        this.prompt = prompt;
        this.referenceMaterialIds = referenceMaterialIds;
        this.score = 0.0f;
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
    
    public String getStudySessionId() {
        return studySessionId;
    }
    
    public void setStudySessionId(String studySessionId) {
        this.studySessionId = studySessionId;
    }
    
    public List<Question> getQuestions() {
        return questions;
    }
    
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public float getScore() {
        return score;
    }
    
    public void setScore(float score) {
        this.score = score;
    }
    
    public String getPrompt() {
        return prompt;
    }
    
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
    
    public List<String> getReferenceMaterialIds() {
        return referenceMaterialIds;
    }
    
    public void setReferenceMaterialIds(List<String> referenceMaterialIds) {
        this.referenceMaterialIds = referenceMaterialIds;
    }
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    public Duration getDuration() {
        return duration;
    }
    
    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    
    public void calculateScore() {
        if (questions == null || questions.isEmpty()) {
            this.score = 0.0f;
            return;
        }
        
        long correctCount = questions.stream().filter(Question::isWasCorrect).count();
        this.score = (float) correctCount / questions.size() * 100;
    }
}

