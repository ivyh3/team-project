package use_case.start_study_session;

import interface_adapter.view_model.StudySessionConfigState;

public class StartStudySessionInputData {
    private final StudySessionConfigState config;

    public StartStudySessionInputData(StudySessionConfigState config) {
        this.config = config;
    }

    public StudySessionConfigState getConfig() {
        return config;
    }
}