package use_case.config_study_session;

import interface_adapter.view_model.StudySessionConfigState;


public class ConfigStudySessionInputData {
    private final StudySessionConfigState state;
    public ConfigStudySessionInputData(StudySessionConfigState state) {
        this.state = state;
    }

    public StudySessionConfigState getState() {
        return state;
    }
}