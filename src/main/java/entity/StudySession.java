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

    // private String calendarEventId;
    // private boolean syncCalendar;

    public StudySession(String id, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = Duration.between(startTime, endTime);
        // this.status = "active";
        // this.syncCalendar = false;
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
        return "StudySession{" +
                "id='" + id + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                '}';
    }

    // public String getCalendarEventId() {
    // return calendarEventId;
    // }

    // public void setCalendarEventId(String calendarEventId) {
    // this.calendarEventId = calendarEventId;
    // }

    // public boolean isSyncCalendar() {
    // return syncCalendar;
    // }

    // public void setSyncCalendar(boolean syncCalendar) {
    // this.syncCalendar = syncCalendar;
    // }
}
