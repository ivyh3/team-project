package use_case.start_study_session;

/**
 * The Input boundary for the start study session use case.
 */
public interface StartStudySessionInputBoundary {
    /**
     * Start a study session.
     *
     * @param startStudySessionInputData The input data
     */
    void execute(StartStudySessionInputData startStudySessionInputData);

    /**
     * Abort the configuration and return home.
     */
    void abortStudySessionConfig();

    /**
     * Refresh the list of file options available to the user.
     */
    void refreshFileOptions();
}
