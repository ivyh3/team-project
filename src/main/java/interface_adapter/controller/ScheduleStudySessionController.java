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
     * @param courseId the course ID
     * @param startTime the start time
     * @param endTime the end time
     * @param syncWithCalendar whether to sync with Google Calendar
     */
    public void execute(String userId, String courseId, LocalDateTime startTime, 
                       LocalDateTime endTime, boolean syncWithCalendar) {
        ScheduleStudySessionInputData inputData = new ScheduleStudySessionInputData(
            userId, courseId, startTime, endTime, syncWithCalendar
        );
        interactor.execute(inputData);
    }
}
