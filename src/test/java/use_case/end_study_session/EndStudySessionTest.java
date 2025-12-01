package use_case.end_study_session;

import java.time.LocalDateTime;

import entity.StudySessionFactory;
import frameworks_drivers.database.InMemoryDatabase;
import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionState;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class EndStudySessionTest {

    @Test
    void successSavedSessionTest() {
        final StudySessionState sessionState = new StudySessionState();

        // Assume session started 1 hour ago (doesn't matter really)
        sessionState.setStartTime(LocalDateTime.now().minusHours(1));

        sessionState.setSessionType(StudySessionConfigState.SessionType.VARIABLE);
        sessionState.setActive(true);
        sessionState.setReferenceFile("csc236.pdf");
        sessionState.setPrompt("Recursive correctness");

        final EndStudySessionDataAccessInterface sessionRepository = new InMemoryDatabase();

        final EndStudySessionInputData inputData = new EndStudySessionInputData(
            "testUser",
            sessionState);

        final EndStudySessionOutputBoundary presenter = new EndStudySessionOutputBoundary() {
            @Override
            public void prepareEndView(EndStudySessionOutputData outputData) {
                // Assert the session has ended
                assertFalse(outputData.getStudySessionState().isActive());

                // Assert end time was provided
                assertNotNull(outputData.getEndTime());

                // Assert the session was saved
                assertNotNull(sessionRepository.getStudySessionById("test_user", outputData.getSessionId()));
            }
        };

        final EndStudySessionInteractor interactor = new EndStudySessionInteractor(presenter,
            sessionRepository,
            new StudySessionFactory());
        interactor.execute(inputData);
    }
}
