package use_case.sync_google_calendar;

/**
 * Output boundary for the Sync Google Calendar use case.
 */
public interface SyncGoogleCalendarOutputBoundary {
    /**
     * Prepares the success view.
     * @param outputData the output data
     */
    void prepareSuccessView(SyncGoogleCalendarOutputData outputData);
    
    /**
     * Prepares the failure view.
     * @param error the error message
     */
    void prepareFailView(String error);
}

