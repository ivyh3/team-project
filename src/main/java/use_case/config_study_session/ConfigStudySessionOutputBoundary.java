package use_case.config_study_session;

import java.util.List;

public interface ConfigStudySessionOutputBoundary {
    void updateConfig(ConfigStudySessionOutputData state);
    void startStudySession(ConfigStudySessionOutputData state);
    void abortStudySessionConfig();
    void prepareErrorView(List<String> errors);
}
