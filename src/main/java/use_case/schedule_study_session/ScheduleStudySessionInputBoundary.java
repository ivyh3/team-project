package use_case.schedule_study_session;

/**
 * Input boundary for the Schedule Study Session use case.
 */
public interface ScheduleStudySessionInputBoundary {
    /**
     * Executes the schedule study session use case.
     * 
     * @param inputData the input data for scheduling a study session
     */
    void execute(ScheduleStudySessionInputData inputData);

    /**
     * Executes the delete study session use case.
     * * @param inputData the input data for deleting a study session
     */
    void delete(DeleteScheduledSessionInputData inputData);

    /**
     * Initiates the use case to fetch all scheduled sessions for a user..
     * * @param userId The ID of the user whose sessions are to be loaded.
     */
    void executeLoad(String userId);
}
