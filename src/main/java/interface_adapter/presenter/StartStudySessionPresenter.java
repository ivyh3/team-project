package interface_adapter.presenter;

import app.AppBuilder;
import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionConfigViewModel;
import interface_adapter.view_model.StudySessionState;
import interface_adapter.view_model.StudySessionViewModel;
import use_case.start_study_session.StartStudySessionOutputBoundary;
import use_case.start_study_session.StartStudySessionOutputData;

public class StartStudySessionPresenter implements StartStudySessionOutputBoundary {
    StudySessionConfigViewModel studySessionConfigViewModel;
    StudySessionViewModel studySessionViewModel;

    public StartStudySessionPresenter(StudySessionConfigViewModel studySessionConfigViewModel, StudySessionViewModel studySessionViewModel) {
        this.studySessionConfigViewModel = studySessionConfigViewModel;
        this.studySessionViewModel = studySessionViewModel;
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

        studySessionConfigViewModel.setState(new StudySessionConfigState()); // Reset state

        // Navigate to study session view
        AppBuilder.viewManagerModel.setView(studySessionViewModel.getViewName());

    }

    @Override
    public void abortStudySessionConfig() {
        // Reset config state.
        studySessionConfigViewModel.setState(new StudySessionConfigState());

        // Navigate back to the dashboard.
        // TODO: MAke the presenter contain reference to the dashboard view/viewmodel
        AppBuilder.viewManagerModel.setView("dashboard");
    }

    @Override
    public void setSessionType(StudySessionConfigState.SessionType sessionType) {
        studySessionConfigViewModel.getState().setSessionType(sessionType);

        studySessionConfigViewModel.firePropertyChange();
    }
}
