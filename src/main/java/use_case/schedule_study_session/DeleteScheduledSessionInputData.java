package use_case.schedule_study_session;

public class DeleteScheduledSessionInputData {
    private final String userId;
    private final String sessionId;

    public DeleteScheduledSessionInputData(String userId, String sessionId) {
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

