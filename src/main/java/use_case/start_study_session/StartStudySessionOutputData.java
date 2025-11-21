package use_case.start_study_session;

import interface_adapter.view_model.StudySessionConfigState;

import java.time.LocalDateTime;

/**
 * Output data for the start study session use case.
 * Includes the configuration state set and the start time.
 */
public class StartStudySessionOutputData {
    private final StudySessionConfigState config;
    private final LocalDateTime startTime;

    public StartStudySessionOutputData(StudySessionConfigState config, LocalDateTime startTime) {
        this.config = config;
        this.startTime = startTime;
    }

    public StudySessionConfigState getConfig() {
        return config;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
}
