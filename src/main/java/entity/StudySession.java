package entity;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Represents a study session.
 */
public class StudySession {
    private String id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;

    public StudySession(String id, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time for this study session. Session duration is then calculated if
     * both start and end time exist.
     *
     * @param endTime The end time for the start session
     */
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

    @Override
    public String toString() {
        return "StudySession{"
            + "id='" + id + '\''
            + ", startTime=" + startTime
            + ", endTime=" + endTime
            + ", duration=" + duration
            + '}';
    }
}
