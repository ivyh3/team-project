package use_case.end_study_session;

import interface_adapter.view_model.StudySessionState;

/**
 * Input data for ending a study session.
 */
public class EndStudySessionInputData {
    private final StudySessionState studySessionState;
    private final String userId;

    public EndStudySessionInputData(String userId, StudySessionState studySessionState) {
        this.studySessionState = studySessionState;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public StudySessionState getStudySessionState() {
        return studySessionState;
    }
}
