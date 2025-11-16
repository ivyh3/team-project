package use_case.config_study_session;

import java.util.List;

import interface_adapter.view_model.StudySessionConfigState;

public interface ConfigStudySessionOutputBoundary {
    void updateConfig(ConfigStudySessionOutputData state);
    void startStudySession(ConfigStudySessionOutputData state);
    void prepareErrorView(List<String> errors);
}
