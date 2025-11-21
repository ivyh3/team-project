package use_case.start_study_session;

import interface_adapter.view_model.StudySessionConfigState;

public interface StartStudySessionInputBoundary {
    void execute(StartStudySessionInputData startStudySessionInputData);

    void abortStudySessionConfig();

    void setSessionType(StudySessionConfigState.SessionType sessionType);
}
