package use_case.end_study_session;

import entity.StudySession;
import interface_adapter.view_model.StudySessionState;

import java.time.LocalDateTime;

public class EndStudySessionInteractor implements EndStudySessionInputBoundary {
    private final EndStudySessionOutputBoundary presenter;
    private final EndStudySessionDataAccessInterface sessionDataAccessObject;

    public EndStudySessionInteractor(EndStudySessionOutputBoundary presenter,
                                     EndStudySessionDataAccessInterface sessionDataAccessObject) {
        this.presenter = presenter;
        this.sessionDataAccessObject = sessionDataAccessObject;
    }

    public void execute(EndStudySessionInputData inputData) {
        StudySessionState sessionState = inputData.getStudySessionState();
        LocalDateTime endTime = LocalDateTime.now();
        sessionState.setActive(false); // The session is now finished.

        // Todo: Adapt the studysession entity to fit needs better
        StudySession session = new StudySession(null, null, null, sessionState.getStartTime());
        session.setEndTime(endTime);
        StudySession savedSession = sessionDataAccessObject.addStudySession(session);
        int sessionId = Integer.parseInt(savedSession.getId());

        EndStudySessionOutputData outputData = new EndStudySessionOutputData(
                sessionState,
                endTime,
                sessionId
        );

        presenter.prepareEndView(outputData);
    }
}
