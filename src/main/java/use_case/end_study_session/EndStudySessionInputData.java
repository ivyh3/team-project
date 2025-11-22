package use_case.end_study_session;

import interface_adapter.view_model.StudySessionState;

/**
 * Input data for ending a study session.
 */
public class EndStudySessionInputData {
    private final StudySessionState studySessionState;

    public EndStudySessionInputData(StudySessionState studySessionState) {
        this.studySessionState = studySessionState;
    }

    public StudySessionState getStudySessionState() {
        return studySessionState;
    }
}
