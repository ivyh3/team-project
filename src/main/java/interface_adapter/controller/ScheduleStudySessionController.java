package interface_adapter.controller;

import java.time.LocalDateTime;

import use_case.schedule_study_session.DeleteScheduledSessionInputData;
import use_case.schedule_study_session.ScheduleStudySessionInputBoundary;
import use_case.schedule_study_session.ScheduleStudySessionInputData;

/**
 * Controller for the Schedule Study Session use case.
 */
public class ScheduleStudySessionController {
    private final ScheduleStudySessionInputBoundary interactor;

    public ScheduleStudySessionController(ScheduleStudySessionInputBoundary interactor) {
        this.interactor = interactor;
    }
    /**
     * Executes the schedule study session use case.
     * @param startTime the start time
     * @param endTime the end time
     * @param title the topic of the study session
     * @param userId the ID of the user
     */

    public void execute(String userId, LocalDateTime startTime, LocalDateTime endTime, String title) {
        final ScheduleStudySessionInputData inputData = new ScheduleStudySessionInputData(
                userId, startTime, endTime, title);
        interactor.execute(inputData);
    }

    /**
     * Executes the delete study session use case.
     * @param userId the ID of the user
     * @param sessionId the ID of the session to delete
     */
    public void delete(String userId, String sessionId) {
        final DeleteScheduledSessionInputData inputData = new DeleteScheduledSessionInputData(userId, sessionId);
        interactor.delete(inputData);
    }

    /**
     * Triggers the Use Case to load all initial sessions.
     * @param userId The ID of the currently logged-in user.
     */
    public void loadInitialSessions(String userId) {
        interactor.executeLoad(userId);
    }
}
