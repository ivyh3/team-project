package interface_adapter.controller;

import interface_adapter.view_model.StudySessionConfigState;
import use_case.config_study_session.ConfigStudySessionInputBoundary;
import use_case.config_study_session.ConfigStudySessionInputData;
import use_case.config_study_session.ConfigStudySessionOutputData;

public class StudySessionConfigController {
    private final ConfigStudySessionInputBoundary interactor;
    public StudySessionConfigController(ConfigStudySessionInputBoundary interactor) {
        this.interactor = interactor;
    }
    public void execute(StudySessionConfigState state) {
        interactor.execute(state);
    }
}
