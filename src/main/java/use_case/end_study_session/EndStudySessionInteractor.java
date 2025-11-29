package use_case.end_study_session;

import entity.StudySession;
import entity.StudySessionFactory;
import interface_adapter.view_model.DashboardState;
import interface_adapter.view_model.StudySessionState;

import java.time.LocalDateTime;

public class EndStudySessionInteractor implements EndStudySessionInputBoundary {
    private final EndStudySessionOutputBoundary presenter;
    private final EndStudySessionDataAccessInterface sessionDataAccessObject;
    private final StudySessionFactory studySessionFactory;

    public EndStudySessionInteractor(EndStudySessionOutputBoundary presenter,
                                     EndStudySessionDataAccessInterface sessionDataAccessObject, StudySessionFactory studySessionFactory) {
        this.presenter = presenter;
        this.sessionDataAccessObject = sessionDataAccessObject;
        this.studySessionFactory = studySessionFactory;
    }

    public void execute(EndStudySessionInputData inputData) {
        StudySessionState sessionState = inputData.getStudySessionState();
        LocalDateTime endTime = LocalDateTime.now();
        sessionState.setActive(false); // The session is now finished.

        StudySession session = studySessionFactory.create(
                sessionState.getStartTime(),
                endTime);

        // TODO: MAke User ID handling, well, real
        StudySession savedSession = sessionDataAccessObject.addStudySession(DashboardState.userId, session);
        String sessionId = savedSession.getId();

        EndStudySessionOutputData outputData = new EndStudySessionOutputData(
                sessionState,
                endTime,
                sessionId);

        presenter.prepareEndView(outputData);
    }
}