package interface_adapter.controller;

import interface_adapter.view_model.StudySessionState;
import use_case.end_study_session.EndStudySessionInputBoundary;

public class EndStudySessionController {
    private final EndStudySessionInputBoundary interactor;
    public EndStudySessionController(EndStudySessionInputBoundary interactor) {
        this.interactor = interactor;
    }
    public void execute(StudySessionState state) {
        interactor.execute(state);
    }
}