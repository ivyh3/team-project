package use_case.end_study_session;

import interface_adapter.view_model.StudySessionState;

import java.time.LocalDateTime;

public class EndStudySessionInteractor implements EndStudySessionInputBoundary {
    public final EndStudySessionOutputBoundary presenter;

    public EndStudySessionInteractor(EndStudySessionOutputBoundary presenter) {
        this.presenter = presenter;
    }

    public void execute(EndStudySessionInputData inputData) {
        StudySessionState sessionState = inputData.getStudySessionState();
        sessionState.setActive(false); // The session is now finished.

        // TODO: Save the sttudy session data to a database here
        // StudySession savedSession = sessionRepository.save(new StudySession(........));
        // int id = savedSession.getId();

        EndStudySessionOutputData outputData = new EndStudySessionOutputData(
                sessionState,
                LocalDateTime.now(),
                2010231
        );

        presenter.prepareEndView(outputData);
    }
}
