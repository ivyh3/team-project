package use_case.schedule_study_session;

import frameworks_drivers.google_calendar.GoogleCalendarService;

/**
 * Interactor for the Schedule Study Session use case.
 */
public class ScheduleStudySessionInteractor implements ScheduleStudySessionInputBoundary {
    private final GoogleCalendarService calendarService;
    private final ScheduleStudySessionOutputBoundary outputBoundary;

    public ScheduleStudySessionInteractor(GoogleCalendarService calendarService,
            ScheduleStudySessionOutputBoundary outputBoundary) {
        this.calendarService = calendarService;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(ScheduleStudySessionInputData inputData) {
        // TODO: Implement the business logic for scheduling a study session
        // 1. Validate start and end times
        // 2. Create StudySession entity
        // 3. Save to repository
        // 4. If syncWithCalendar, create calendar event
        // 5. Prepare success or failure view
    }
}
