package use_case.sync_google_calendar;

/**
 * Input data for the Sync Google Calendar use case.
 */
public class SyncGoogleCalendarInputData {
    private final String userId;
    private final String sessionId;
    
    public SyncGoogleCalendarInputData(String userId, String sessionId) {
        this.userId = userId;
        this.sessionId = sessionId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getSessionId() {
        return sessionId;
    }
}

