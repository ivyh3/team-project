package interface_adapter.controller;

import use_case.schedule_study_session.ScheduleStudySessionInputBoundary;
import use_case.schedule_study_session.ScheduleStudySessionInputData;

import java.time.LocalDateTime;

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
     * @param userId the user ID
     * @param startTime the start time
     * @param endTime the end time
     * @param title the topic of the study session
     */
    public void execute(String userId, LocalDateTime startTime,
                       LocalDateTime endTime, String title) {
        ScheduleStudySessionInputData inputData = new ScheduleStudySessionInputData(
            userId, startTime, endTime, title);
        interactor.execute(inputData);
    }
}
