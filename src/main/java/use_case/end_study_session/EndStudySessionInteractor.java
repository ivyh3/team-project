package use_case.end_study_session;

import interface_adapter.view_model.StudySessionState;

public class EndStudySessionInteractor implements EndStudySessionInputBoundary {
    public EndStudySessionOutputBoundary presenter;
    public EndStudySessionInteractor(EndStudySessionOutputBoundary presenter) {
        this.presenter = presenter;
    }
    public void execute(StudySessionState state) {
        presenter.prepareEndView(state);
    }
}
