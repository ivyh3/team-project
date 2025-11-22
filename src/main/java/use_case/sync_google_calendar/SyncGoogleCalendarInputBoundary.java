package use_case.sync_google_calendar;

/**
 * Input boundary for the Sync Google Calendar use case.
 */
public interface SyncGoogleCalendarInputBoundary {
    /**
     * Executes the sync Google calendar use case.
     * 
     * @param inputData the input data for syncing with Google Calendar
     */
    void execute(SyncGoogleCalendarInputData inputData);
}
