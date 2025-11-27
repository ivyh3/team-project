package interface_adapter.presenter;

import interface_adapter.view_model.ViewManagerModel;
import interface_adapter.view_model.StudySessionEndState;
import interface_adapter.view_model.StudySessionEndViewModel;
import interface_adapter.view_model.StudySessionState;
import interface_adapter.view_model.StudySessionViewModel;
import use_case.end_study_session.EndStudySessionOutputBoundary;
import use_case.end_study_session.EndStudySessionOutputData;

import java.time.LocalDateTime;

public class EndStudySessionPresenter implements EndStudySessionOutputBoundary {
    private final StudySessionViewModel studySessionViewModel;
    private final StudySessionEndViewModel studySessionEndViewModel;
    private final ViewManagerModel viewManagerModel;

    public EndStudySessionPresenter(StudySessionViewModel studySessionViewModel,
            StudySessionEndViewModel studySessionEndViewModel, ViewManagerModel viewManagerModel) {
        this.studySessionViewModel = studySessionViewModel;
        this.studySessionEndViewModel = studySessionEndViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    public void prepareEndView(EndStudySessionOutputData outputData) {
        // Get the end state of the session.
        StudySessionState finalizedSession = outputData.getStudySessionState();
        StudySessionEndState endState = new StudySessionEndState(finalizedSession, LocalDateTime.now());

        // Todo: somehow think of a way to reset state for StudySessionView (although it
        // should work fine rn)
        // Set states for the views
        studySessionViewModel.setState(finalizedSession);
        studySessionViewModel.firePropertyChange();
        
        studySessionEndViewModel.setState(endState);
        studySessionEndViewModel.firePropertyChange();

        // Todo: use actual method to swap view
        // Swap to session end view
        viewManagerModel.setView(studySessionEndViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}