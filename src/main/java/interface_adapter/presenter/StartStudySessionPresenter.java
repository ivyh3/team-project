package interface_adapter.presenter;

import interface_adapter.view_model.*;
import use_case.start_study_session.StartStudySessionOutputBoundary;
import use_case.start_study_session.StartStudySessionOutputData;

/**
 * Presenter for the start study session use case.
 */
public class StartStudySessionPresenter implements StartStudySessionOutputBoundary {
    private final StudySessionConfigViewModel studySessionConfigViewModel;
    private final StudySessionViewModel studySessionViewModel;
    private final ViewManagerModel viewManagerModel;
    private final String dashboardViewName;

    public StartStudySessionPresenter(StudySessionConfigViewModel studySessionConfigViewModel,
                                      StudySessionViewModel studySessionViewModel,
                                      ViewManagerModel viewManagerModel,
                                      String dashboardViewName) {
        this.studySessionConfigViewModel = studySessionConfigViewModel;
        this.studySessionViewModel = studySessionViewModel;
        this.viewManagerModel = viewManagerModel;
        this.dashboardViewName = dashboardViewName;
    }

    @Override
    public void prepareErrorView(String errorMessage) {
        studySessionConfigViewModel.getState().setError(errorMessage);
        studySessionConfigViewModel.firePropertyChange("error");
    }

    @Override
    public void startStudySession(StartStudySessionOutputData outputData) {
        // Prepare study session view state with set config
        studySessionViewModel.setState(new StudySessionState(outputData.getConfig(), outputData.getStartTime()));
        studySessionViewModel.firePropertyChange();

        // Reset config view state to default
        studySessionConfigViewModel.setState(new StudySessionConfigState());
        studySessionConfigViewModel.firePropertyChange();

        // Navigate to study session view
        viewManagerModel.setView(studySessionViewModel.getViewName());
        viewManagerModel.firePropertyChange();

    }

    @Override
    public void abortStudySessionConfig() {
        // Reset config state.
        studySessionConfigViewModel.setState(new StudySessionConfigState());
        studySessionConfigViewModel.firePropertyChange();

        // Navigate back to the dashboard.
        viewManagerModel.setView(dashboardViewName);
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void refreshFileOptions(java.util.List<String> fileOptions) {
        studySessionConfigViewModel.getState().setFileOptions(fileOptions);
        studySessionConfigViewModel.firePropertyChange("fileOptions");
    }
}