package use_case.schedule_study_session;

import java.time.LocalDateTime;

/**
 * Input data for the Schedule Study Session use case.
 */
public class ScheduleStudySessionInputData {
    private final String userId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String title;

    public ScheduleStudySessionInputData(String userId,
                                        LocalDateTime startTime, LocalDateTime endTime, String title) {
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }


    public String getTitle() { return title; }
}
