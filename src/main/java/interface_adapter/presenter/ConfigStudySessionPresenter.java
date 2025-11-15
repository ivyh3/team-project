package interface_adapter.presenter;

import interface_adapter.view_model.StudySessionConfigViewModel;
import use_case.config_study_session.ConfigStudySessionOutputBoundary;
import use_case.config_study_session.ConfigStudySessionOutputData;

public class ConfigStudySessionPresenter implements ConfigStudySessionOutputBoundary {
    StudySessionConfigViewModel viewModel;
    public ConfigStudySessionPresenter(StudySessionConfigViewModel viewModel) {
        this.viewModel = viewModel;
    }
    public void updateConfig(ConfigStudySessionOutputData data) {
        viewModel.setState(data.getState());
    }
}
