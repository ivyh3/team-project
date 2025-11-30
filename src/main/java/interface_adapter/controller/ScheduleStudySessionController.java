package interface_adapter.controller;

import use_case.schedule_study_session.ScheduleStudySessionInputBoundary;
import use_case.schedule_study_session.ScheduleStudySessionInputData;
import interface_adapter.view_model.DashboardViewModel;

import java.time.LocalDateTime;

/**
 * Controller for the Schedule Study Session use case.
 */
public class ScheduleStudySessionController {
    private final ScheduleStudySessionInputBoundary interactor;
    private final DashboardViewModel dashboardVm;

    public ScheduleStudySessionController(
            ScheduleStudySessionInputBoundary interactor,
            DashboardViewModel dashboardVm) {
        this.interactor = interactor;
        this.dashboardVm = dashboardVm;
    }
    /**
     * Executes the schedule study session use case.
     * @param startTime the start time
     * @param endTime the end time
     * @param title the topic of the study session
     */
    public void execute(LocalDateTime startTime, LocalDateTime endTime, String title) {
        String userId = dashboardVm.getState().getUserId();

        ScheduleStudySessionInputData inputData = new ScheduleStudySessionInputData(
                userId, startTime, endTime, title
        );

        interactor.execute(inputData);
    }
}
