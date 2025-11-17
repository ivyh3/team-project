package use_case.config_study_session;


public interface ConfigStudySessionInputBoundary {
    void execute(ConfigStudySessionInputData configStudySessionInputData);
    void setSessionDuration(ConfigStudySessionInputData inputData);
    void setSessionType(ConfigStudySessionInputData inputData);
    void setSessionReferenceMaterials(ConfigStudySessionInputData inputData);
    void undo(ConfigStudySessionInputData inputData);
}
