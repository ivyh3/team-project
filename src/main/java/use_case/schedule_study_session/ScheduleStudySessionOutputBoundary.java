package use_case.schedule_study_session;

/**
 * Output boundary for the Schedule Study Session use case.
 */
public interface ScheduleStudySessionOutputBoundary {
    /**
     * Prepares the success view.
     * 
     * @param outputData the output data
     */
    void prepareSuccessView(ScheduleStudySessionOutputData outputData);

    /**
     * Prepares the failure view.
     * 
     * @param error the error message
     */
    void prepareFailView(String error);
}
