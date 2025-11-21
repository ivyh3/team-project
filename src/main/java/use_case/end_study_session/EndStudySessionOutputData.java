package use_case.end_study_session;

import interface_adapter.view_model.StudySessionState;

import java.time.LocalDateTime;

/**
 * Output data for the study session.
 */
public class EndStudySessionOutputData {
    private final StudySessionState studySessionState;
    private final LocalDateTime endTime;
    private final int sessionId;

    public EndStudySessionOutputData(StudySessionState studySessionState, LocalDateTime endTime, int sessionId) {
        this.studySessionState = studySessionState;
        this.endTime = endTime;
        this.sessionId = sessionId;
    }

    public StudySessionState getStudySessionState() {
        return studySessionState;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getSessionId() {
        return sessionId;
    }

}
