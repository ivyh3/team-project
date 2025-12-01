package use_case.schedule_session;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import use_case.schedule_study_session.ScheduleStudySessionInteractor;
import use_case.schedule_study_session.ScheduleStudySessionInputData;
import use_case.schedule_study_session.ScheduleStudySessionOutputData;
import use_case.schedule_study_session.DeleteScheduledSessionInputData;
import use_case.schedule_study_session.DeleteScheduledSessionOutputData;
import use_case.schedule_study_session.ScheduleStudySessionDataAccessInterface;
import use_case.schedule_study_session.ScheduleStudySessionOutputBoundary;
import entity.ScheduledSession;

public class ScheduleSessionTest {

    @Test
    void testExecute_success() {
        ScheduleStudySessionDataAccessInterface dataAccess = mock(ScheduleStudySessionDataAccessInterface.class);
        ScheduleStudySessionOutputBoundary outputBoundary = mock(ScheduleStudySessionOutputBoundary.class);
        ScheduleStudySessionInteractor interactor = new ScheduleStudySessionInteractor(dataAccess, outputBoundary);

        ScheduleStudySessionInputData inputData = new ScheduleStudySessionInputData(
                "user1",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "Math Study"
        );

        ScheduledSession savedSession = new ScheduledSession(
                "session1",
                inputData.getStartTime(),
                inputData.getEndTime(),
                inputData.getTitle()
        );

        when(dataAccess.saveSession(anyString(), any())).thenReturn(savedSession);

        interactor.execute(inputData);

        verify(dataAccess).saveSession(eq("user1"), any());
        verify(outputBoundary).prepareSuccessView(any(ScheduleStudySessionOutputData.class));
    }

    @Test
    void testDelete_sessionExists() {
        ScheduleStudySessionDataAccessInterface dataAccess = mock(ScheduleStudySessionDataAccessInterface.class);
        ScheduleStudySessionOutputBoundary outputBoundary = mock(ScheduleStudySessionOutputBoundary.class);
        ScheduleStudySessionInteractor interactor = new ScheduleStudySessionInteractor(dataAccess, outputBoundary);

        DeleteScheduledSessionInputData inputData = new DeleteScheduledSessionInputData("user1", "session1");
        ScheduledSession session = new ScheduledSession(
                "session1",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "Math Study"
        );

        when(dataAccess.getScheduledSessionById("user1", "session1")).thenReturn(session);

        // Act
        interactor.delete(inputData);

        // Assert
        verify(dataAccess).deleteSession("user1", session);
        verify(outputBoundary).prepareDeleteSuccessView(any(DeleteScheduledSessionOutputData.class));
    }

    @Test
    void testDelete_sessionDoesNotExist() {
        ScheduleStudySessionDataAccessInterface dataAccess = mock(ScheduleStudySessionDataAccessInterface.class);
        ScheduleStudySessionOutputBoundary outputBoundary = mock(ScheduleStudySessionOutputBoundary.class);
        ScheduleStudySessionInteractor interactor = new ScheduleStudySessionInteractor(dataAccess, outputBoundary);

        DeleteScheduledSessionInputData inputData = new DeleteScheduledSessionInputData("user1", "session1");

        when(dataAccess.getScheduledSessionById("user1", "session1")).thenReturn(null);

        interactor.delete(inputData);

        verify(outputBoundary).prepareFailView("Session not found or already deleted.");
        verify(dataAccess, never()).deleteSession(anyString(), any());
    }

    @Test
    void testExecuteLoad() {
        ScheduleStudySessionDataAccessInterface dataAccess = mock(ScheduleStudySessionDataAccessInterface.class);
        ScheduleStudySessionOutputBoundary outputBoundary = mock(ScheduleStudySessionOutputBoundary.class);
        ScheduleStudySessionInteractor interactor = new ScheduleStudySessionInteractor(dataAccess, outputBoundary);

        List<ScheduledSession> sessions = List.of(
                new ScheduledSession("session1", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Math Study")
        );

        when(dataAccess.getAllSessions("user1")).thenReturn(sessions);

        interactor.executeLoad("user1");

        verify(outputBoundary).loadSessions(sessions);
    }
}
