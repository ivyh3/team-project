package use_case.schedule_study_session;

import java.time.LocalDateTime;

/**
 * Output data for the Schedule Study Session use case.
 */
public class ScheduleStudySessionOutputData {
    private final String sessionId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String calendarEventId;

    public ScheduleStudySessionOutputData(String sessionId, LocalDateTime startTime,
            LocalDateTime endTime, String calendarEventId) {
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.calendarEventId = calendarEventId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getCalendarEventId() {
        return calendarEventId;
    }
}
