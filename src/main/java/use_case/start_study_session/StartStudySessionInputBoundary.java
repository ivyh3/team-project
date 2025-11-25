package use_case.start_study_session;

import interface_adapter.view_model.StudySessionConfigState;

/**
 * The Input boundary for the start study session use case.
 */
public interface StartStudySessionInputBoundary {
    /**
     * Start a study session.
     * @param startStudySessionInputData The input data
     */
    void execute(StartStudySessionInputData startStudySessionInputData);

    /**
     * Abort the configuration and return home.
     */
    void abortStudySessionConfig();

    /**
     * Set the currently selected session type.
     * @param sessionType the session type to choose.
     */
    void setSessionType(StudySessionConfigState.SessionType sessionType);


    /**
     * Refresh the list of file options available to the user.
     */    
    void refreshFileOptions();
}
