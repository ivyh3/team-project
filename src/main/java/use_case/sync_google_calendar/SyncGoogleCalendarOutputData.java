package use_case.sync_google_calendar;

/**
 * Output data for the Sync Google Calendar use case.
 */
public class SyncGoogleCalendarOutputData {
    private final String calendarEventId;
    private final String sessionId;
    
    public SyncGoogleCalendarOutputData(String calendarEventId, String sessionId) {
        this.calendarEventId = calendarEventId;
        this.sessionId = sessionId;
    }
    
    public String getCalendarEventId() {
        return calendarEventId;
    }
    
    public String getSessionId() {
        return sessionId;
    }
}

