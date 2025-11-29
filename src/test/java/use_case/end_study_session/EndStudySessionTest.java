package use_case.end_study_session;

import frameworks_drivers.database.InMemoryDatabase;
import interface_adapter.view_model.StudySessionConfigState;
import interface_adapter.view_model.StudySessionEndViewModel;
import interface_adapter.view_model.StudySessionState;
import org.junit.jupiter.api.Test;

import entity.StudySessionFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EndStudySessionTest {

    @Test
    void successSavedSessionTest() {
        StudySessionState sessionState = new StudySessionState();

        // Assume session started 1 hour ago (doesn't matter really)
        sessionState.setStartTime(LocalDateTime.now().minusHours(1));

        sessionState.setSessionType(StudySessionConfigState.SessionType.VARIABLE);
        sessionState.setActive(true);
        sessionState.setReferenceFile("csc236.pdf");
        sessionState.setPrompt("Recursive correctness");

        EndStudySessionDataAccessInterface sessionRepository = new InMemoryDatabase();

        EndStudySessionInputData inputData = new EndStudySessionInputData(
                sessionState,
                "test_user");

        EndStudySessionOutputBoundary presenter = new EndStudySessionOutputBoundary() {
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

        EndStudySessionInteractor interactor = new EndStudySessionInteractor(presenter,
                sessionRepository,
                new StudySessionFactory());

        interactor.execute(inputData);
    }
}
