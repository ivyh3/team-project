package use_case.end_study_session;

/**
 * Input boundary for ending a study session.
 */
public interface EndStudySessionInputBoundary {
    /**
     * Ends the study session.
     *
     * @param inputData The input data
     */
    void execute(EndStudySessionInputData inputData);
}
