package interface_adapter.presenter;

import app.AppBuilder;
import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionConfigViewModel;
import interface_adapter.view_model.StudySessionState;
import interface_adapter.view_model.StudySessionViewModel;
import use_case.config_study_session.ConfigStudySessionOutputBoundary;
import use_case.config_study_session.ConfigStudySessionOutputData;

import java.time.LocalDateTime;

public class ConfigStudySessionPresenter implements ConfigStudySessionOutputBoundary {
    StudySessionConfigViewModel studySessionConfigViewModel;
    StudySessionViewModel studySessionViewModel;
    public ConfigStudySessionPresenter(StudySessionConfigViewModel studySessionConfigViewModel, StudySessionViewModel studySessionViewModel) {
        this.studySessionConfigViewModel = studySessionConfigViewModel;
        this.studySessionViewModel = studySessionViewModel;
    }
    public void updateConfig(ConfigStudySessionOutputData outputData) {
        studySessionConfigViewModel.setState(outputData.getState());
    }

    public void prepareErrorView(java.util.List<String> errors) {
        studySessionConfigViewModel.getState().setErrors(errors);
        studySessionConfigViewModel.firePropertyChange("errors");
    }

    public void startStudySession(ConfigStudySessionOutputData outputData) {
        StudySessionConfigState finalConfig = outputData.getState();
        studySessionViewModel.setState(new StudySessionState(finalConfig, LocalDateTime.now()));

        studySessionConfigViewModel.setState(new StudySessionConfigState()); // Reset state
        AppBuilder.viewManagerModel.setView(studySessionViewModel.getViewName());

    }
}