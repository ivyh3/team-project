package interface_adapter.controller;

import use_case.schedule_study_session.ScheduleStudySessionInputBoundary;
import use_case.schedule_study_session.ScheduleStudySessionInputData;
import use_case.schedule_study_session.DeleteScheduledSessionInputData;
import interface_adapter.view_model.DashboardViewModel;

import java.time.LocalDateTime;

/**
 * Controller for the Schedule Study Session use case.
 */
public class ScheduleStudySessionController {
    private final ScheduleStudySessionInputBoundary interactor;
    private final DashboardViewModel dashboardViewModel;

    public ScheduleStudySessionController(
            ScheduleStudySessionInputBoundary interactor,
            DashboardViewModel dashboardVm) {
        this.interactor = interactor;
        this.dashboardViewModel = dashboardVm;
    }
    /**
     * Executes the schedule study session use case.
     * @param startTime the start time
     * @param endTime the end time
     * @param title the topic of the study session
     */
    public void execute(String userId, LocalDateTime startTime, LocalDateTime endTime, String title) {
        ScheduleStudySessionInputData inputData = new ScheduleStudySessionInputData(
                userId, startTime, endTime, title);
        interactor.execute(inputData);
    }

    /**
     * Executes the delete study session use case.
     * @param sessionId the ID of the session to delete
     */
    public void delete(String sessionId) {
        String userId = dashboardViewModel.getState().getUserId();
        DeleteScheduledSessionInputData inputData = new DeleteScheduledSessionInputData(userId, sessionId);
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
