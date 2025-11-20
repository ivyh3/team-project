package interface_adapter.presenter;

import app.AppBuilder;
import interface_adapter.view_model.StudySessionEndState;
import interface_adapter.view_model.StudySessionEndViewModel;
import interface_adapter.view_model.StudySessionState;
import interface_adapter.view_model.StudySessionViewModel;
import use_case.end_study_session.EndStudySessionOutputBoundary;

import java.time.LocalDateTime;

public class EndStudySessionPresenter implements EndStudySessionOutputBoundary {
    private final StudySessionViewModel studySessionViewModel;
    private final StudySessionEndViewModel studySessionEndViewModel;

    public EndStudySessionPresenter(StudySessionViewModel studySessionViewModel, StudySessionEndViewModel studySessionEndViewModel) {
        this.studySessionViewModel = studySessionViewModel;
        this.studySessionEndViewModel = studySessionEndViewModel;
    }

    public void prepareEndView(StudySessionState newState) {
        StudySessionEndState endState = new StudySessionEndState(newState, LocalDateTime.now());
        // Todo: somehow think of a way to reset state for StudySessionView (although it should work fine rn)
        studySessionViewModel.setState(newState);
        studySessionEndViewModel.setState(endState);
        // Todo: use actual method to swap view
        AppBuilder.viewManagerModel.setView(studySessionEndViewModel.getViewName());
    }
}
