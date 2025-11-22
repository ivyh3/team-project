package use_case.start_study_session;

import interface_adapter.view_model.StudySessionConfigState;

/**
 * Output boundary for the start study session use case.
 */
public interface StartStudySessionOutputBoundary {
    /**
     * Start the study session.
     * @param outputData The output data.
     */
    void startStudySession(StartStudySessionOutputData outputData);

    /**
     * Prepare a failure view or prompt.
     * @param errorMessage The explanation for the error.
     */
    void prepareErrorView(String errorMessage);

    /**
     * Cancel the study session configuration, and return home.
     */
    void abortStudySessionConfig();

    /**
     * Set the session type for the config.
     * @param sessionType The type of the session.
     */
    void setSessionType(StudySessionConfigState.SessionType sessionType);
}
