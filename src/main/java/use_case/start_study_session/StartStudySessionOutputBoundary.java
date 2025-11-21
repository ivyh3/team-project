package use_case.start_study_session;

import interface_adapter.view_model.StudySessionConfigState;

public interface StartStudySessionOutputBoundary {
    void startStudySession(StartStudySessionOutputData state);

    void prepareErrorView(String errorMessage);

    void abortStudySessionConfig();

    void setSessionType(StudySessionConfigState.SessionType sessionType);
}
