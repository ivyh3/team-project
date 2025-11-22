package use_case.end_study_session;

/**
 * The output boundary for ending a study session.
 */
public interface EndStudySessionOutputBoundary {
    /**
     * Prepares the study session end view.
     *
     * @param outputData
     */
    void prepareEndView(EndStudySessionOutputData outputData);
}
