package use_case.start_study_session;

/**
 * Input boundary for the Start Study Session use case.
 */
public interface StartStudySessionInputBoundary {
    /**
     * Executes the start study session use case.
     * @param inputData the input data for starting a study session
     */
    void execute(StartStudySessionInputData inputData);
}

