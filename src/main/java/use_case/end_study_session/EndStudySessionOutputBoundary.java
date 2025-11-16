package use_case.end_study_session;

import interface_adapter.view_model.StudySessionState;

public interface EndStudySessionOutputBoundary {
    void prepareEndView(StudySessionState state);
}
