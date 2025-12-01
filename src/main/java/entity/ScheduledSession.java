package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScheduledSession {
    private final String id;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String title;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ScheduledSession(String id, LocalDateTime startTime, LocalDateTime endTime, String title) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
    }

    public String getId() { return id; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getTitle() { return title; }

    @Override
    public String toString() {
        return startTime.format(formatter) + " - " + endTime.format(formatter) + " : " + title;
    }
}
