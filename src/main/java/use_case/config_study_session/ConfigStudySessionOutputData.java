package use_case.config_study_session;

import java.util.List;

import interface_adapter.view_model.StudySessionConfigState;

public class ConfigStudySessionOutputData {
    private final StudySessionConfigState state;
    public ConfigStudySessionOutputData(StudySessionConfigState state) {
        this.state = state;
    }
    public StudySessionConfigState getState() {
        return state;
    }
}
