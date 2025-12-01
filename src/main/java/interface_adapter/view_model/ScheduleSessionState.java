package interface_adapter.view_model;
import java.time.LocalDateTime;

public class ScheduleSessionState {
    private final String id;
    private final String title;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public ScheduleSessionState(String id, String title, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Add getters:
    public String getId() { return id; }
    public String getTitle() { return title; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
}