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

    public StudyQuiz(String id, float score, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.score = score;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = Duration.between(startTime, endTime);
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
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
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "StudyQuiz{"
            + "id='" + id + '\''
            + ", questions=" + questions
            + ", score=" + score
            + ", startTime=" + startTime
            + ", endTime=" + endTime
            + ", duration=" + duration
            + '}';
    }
}
