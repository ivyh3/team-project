package use_case.start_study_session;

import interface_adapter.view_model.StudySessionState;

public class StartStudySessionInputData {
    private final StudySessionState sessionState;

    public StartStudySessionInputData(StudySessionState sessionState) {
        this.sessionState = sessionState;
    }

    public StudySessionState getSessionState() {
        return sessionState;
    }
}