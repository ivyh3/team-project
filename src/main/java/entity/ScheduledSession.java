package entity;

import java.time.LocalDateTime;

/**
 * Entity for a scheduled study session. Scheduled sessions may or may not be completed,
 * and are just planned sessions to show on a calendar.
 */
public class ScheduledSession {
    private final String id;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public ScheduledSession(String id, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getId() {
        return id;
    }
}
