package interface_adapter.controller;

import interface_adapter.view_model.StudySessionState;
import use_case.end_study_session.EndStudySessionInputBoundary;
import use_case.end_study_session.EndStudySessionInputData;

public class EndStudySessionController {
    private final EndStudySessionInputBoundary interactor;

    public EndStudySessionController(EndStudySessionInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(StudySessionState state) {
        EndStudySessionInputData inputData = new EndStudySessionInputData(state);
        interactor.execute(inputData);

    }
}
