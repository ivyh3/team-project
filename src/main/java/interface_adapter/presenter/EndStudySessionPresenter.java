package interface_adapter.presenter;

import java.time.LocalDateTime;

import interface_adapter.view_model.StudySessionEndState;
import interface_adapter.view_model.StudySessionEndViewModel;
import interface_adapter.view_model.StudySessionState;
import interface_adapter.view_model.StudySessionViewModel;
import interface_adapter.view_model.ViewManagerModel;
import use_case.end_study_session.EndStudySessionOutputBoundary;
import use_case.end_study_session.EndStudySessionOutputData;

/**
 * Presenter for ending a study session.
 */
public class EndStudySessionPresenter implements EndStudySessionOutputBoundary {
    private final StudySessionViewModel studySessionViewModel;
    private final StudySessionEndViewModel studySessionEndViewModel;
    private final ViewManagerModel viewManagerModel;

    public EndStudySessionPresenter(StudySessionViewModel studySessionViewModel,
                                    StudySessionEndViewModel studySessionEndViewModel,
                                    ViewManagerModel viewManagerModel) {
        this.studySessionViewModel = studySessionViewModel;
        this.studySessionEndViewModel = studySessionEndViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Prepares the study session end view.
     *
     * @param outputData Output data
     */
    public void prepareEndView(EndStudySessionOutputData outputData) {
        // Get the end state of the session.
        final StudySessionState finalizedSession = outputData.getStudySessionState();
        final StudySessionEndState endState = new StudySessionEndState(finalizedSession, LocalDateTime.now());

        // Set states for the views
        studySessionViewModel.setState(finalizedSession);
        studySessionViewModel.firePropertyChange();

        studySessionEndViewModel.setState(endState);
        studySessionEndViewModel.firePropertyChange();

        // Swap to session end view
        viewManagerModel.setView(studySessionEndViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
