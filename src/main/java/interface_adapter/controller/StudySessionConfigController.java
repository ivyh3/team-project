package interface_adapter.controller;

import interface_adapter.view_model.StudySessionConfigState;
import use_case.config_study_session.ConfigStudySessionInputBoundary;
import use_case.config_study_session.ConfigStudySessionInputData;

public class StudySessionConfigController {
    private final ConfigStudySessionInputBoundary interactor;

    public StudySessionConfigController(ConfigStudySessionInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(StudySessionConfigState state) {
        ConfigStudySessionInputData inputData = new ConfigStudySessionInputData(
            state.copy()
        );

        interactor.execute(inputData);
    }

    public void undo(StudySessionConfigState state) {
        ConfigStudySessionInputData inputData = new ConfigStudySessionInputData(
                state.copy()
        );
        interactor.undo(inputData);
    }
//
//    public void updateSessionDuration(int targetMinutes) {
//        interactor.setSessionDuration();
//    }
}
