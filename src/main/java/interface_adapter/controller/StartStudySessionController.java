package interface_adapter.controller;

import interface_adapter.view_model.StudySessionConfigState;
import use_case.start_study_session.StartStudySessionInputBoundary;
import use_case.start_study_session.StartStudySessionInputData;

public class StartStudySessionController {
    private final StartStudySessionInputBoundary interactor;

    public StartStudySessionController(StartStudySessionInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(StudySessionConfigState state) {
        StartStudySessionInputData inputData = new StartStudySessionInputData(
            state.copy()
        );
        interactor.execute(inputData);
    }
    public void abortStudySessionConfig() {
        interactor.abortStudySessionConfig();
    }
    public void setSessionType(StudySessionConfigState.SessionType sessionType) {
        interactor.setSessionType(sessionType);
    }
}
