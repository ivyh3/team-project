package use_case.start_study_session;

import interface_adapter.view_model.StudySessionConfigState;

/**
 * The input data for starting a study session. Includes the currently set configuration by the user.
 */
public class StartStudySessionInputData {
    private final String userId;
    private final StudySessionConfigState config;

    public StartStudySessionInputData(String userId, StudySessionConfigState config) {
        this.userId = userId;
        this.config = config;
    }

    public StudySessionConfigState getConfig() {
        return config;
    }

    public String getUserId() {
        return userId;
    }
}
