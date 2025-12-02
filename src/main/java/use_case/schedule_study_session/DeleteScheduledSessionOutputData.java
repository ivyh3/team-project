package use_case.schedule_study_session;

/**
 * Output Data for the Delete Study Session use case.
 */
public class DeleteScheduledSessionOutputData {
    private final String deletedSessionId;
    private final String statusMessage;

    public DeleteScheduledSessionOutputData(String deletedSessionId, String statusMessage) {
        this.deletedSessionId = deletedSessionId;
        this.statusMessage = statusMessage;
    }

    public String getDeletedSessionId() {
        return deletedSessionId;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
