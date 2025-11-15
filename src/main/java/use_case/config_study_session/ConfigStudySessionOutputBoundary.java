package use_case.config_study_session;

import interface_adapter.view_model.StudySessionConfigState;

public interface ConfigStudySessionOutputBoundary {
    void updateConfig(StudySessionConfigState state);
    void startStudySession(StudySessionConfigState state);
}
