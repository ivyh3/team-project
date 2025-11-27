package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a quiz taken after a study session.
 */
public class StudyQuiz {
    private String id;
    private List<Question> questions;
    private float score;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;

    public StudyQuiz(String id, List<Question> questions, float score, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.questions = questions;
        this.score = score;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = (startTime != null && endTime != null) ? Duration.between(startTime, endTime) : null;
    }

    // Getters
    public String getId() {
        return id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public float getScore() {
        return score;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    // Setters (for database / ID assignment)
    public void setId(String id) {
        this.id = id;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        recalcDuration();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        recalcDuration();
    }

    private void recalcDuration() {
        if (startTime != null && endTime != null) {
            this.duration = Duration.between(startTime, endTime);
        }
    }
}
