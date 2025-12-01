package use_case.start_study_session;

import interface_adapter.view_model.StudySessionState;

import java.time.LocalDateTime;

public class StartStudySessionOutputData {
    private final StudySessionState sessionState;
    private final LocalDateTime startTime;

    public StartStudySessionOutputData(StudySessionState sessionState, LocalDateTime startTime) {
        this.sessionState = sessionState;
        this.startTime = startTime;
    }

    public StudySessionState getSessionState() {
        return sessionState;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}