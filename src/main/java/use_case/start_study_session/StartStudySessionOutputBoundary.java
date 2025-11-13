package use_case.start_study_session;

/**
 * Output boundary for the Start Study Session use case.
 */
public interface StartStudySessionOutputBoundary {
    /**
     * Prepares the success view.
     * @param outputData the output data
     */
    void prepareSuccessView(StartStudySessionOutputData outputData);
    
    /**
     * Prepares the failure view.
     * @param error the error message
     */
    void prepareFailView(String error);
}

