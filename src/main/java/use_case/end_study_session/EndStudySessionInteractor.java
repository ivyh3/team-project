package use_case.end_study_session;

import java.time.LocalDateTime;

import entity.StudySession;
import entity.StudySessionFactory;
import interface_adapter.view_model.DashboardState;
import interface_adapter.view_model.StudySessionState;

/**
 * Interactor for ending study sessions.
 */
public class EndStudySessionInteractor implements EndStudySessionInputBoundary {
    private final EndStudySessionOutputBoundary presenter;
    private final EndStudySessionDataAccessInterface sessionDataAccessObject;
    private final StudySessionFactory studySessionFactory;

    public EndStudySessionInteractor(EndStudySessionOutputBoundary presenter,
                                     EndStudySessionDataAccessInterface sessionDataAccessObject,
                                     StudySessionFactory studySessionFactory) {
        this.presenter = presenter;
        this.sessionDataAccessObject = sessionDataAccessObject;
        this.studySessionFactory = studySessionFactory;
    }

    /**
     * Executes the end study session use case.
     *
     * @param inputData The input data
     */
    public void execute(EndStudySessionInputData inputData) {
        final StudySessionState sessionState = inputData.getStudySessionState();
        final LocalDateTime endTime = LocalDateTime.now();
        // The session is now finished, so set active as false.
        sessionState.setActive(false);

        final StudySession session = studySessionFactory.create(
            sessionState.getStartTime(),
            endTime);

        final StudySession savedSession = sessionDataAccessObject.addStudySession(inputData.getUserId(), session);
        final String sessionId = savedSession.getId();

        final EndStudySessionOutputData outputData = new EndStudySessionOutputData(
            sessionState,
            endTime,
            sessionId);

        presenter.prepareEndView(outputData);
    }
}
