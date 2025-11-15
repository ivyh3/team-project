package use_case.config_study_session;

public class ConfigStudySessionInteractor implements ConfigStudySessionInputBoundary {
    private final ConfigStudySessionOutputBoundary presenter;
    public ConfigStudySessionInteractor(ConfigStudySessionOutputBoundary presenter) {
        this.presenter = presenter;
    }
    public void execute(ConfigStudySessionInputData data) {
        ConfigStudySessionOutputData newData = new ConfigStudySessionOutputData(data.getState());
        presenter.updateConfig(newData);
    }
}
