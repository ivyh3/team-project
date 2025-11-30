package use_case.schedule_study_session;

import entity.ScheduledSession;

/**
 * Interactor for the Schedule Study Session use case.
 */
public class ScheduleStudySessionInteractor implements ScheduleStudySessionInputBoundary {
    private final ScheduleStudySessionDataAccessInterface dataAccess;
    private final ScheduleStudySessionOutputBoundary outputBoundary;

	public ScheduleStudySessionInteractor(ScheduleStudySessionDataAccessInterface dataAccess,
			ScheduleStudySessionOutputBoundary outputBoundary) {
		this.dataAccess = dataAccess;
		this.outputBoundary = outputBoundary;
	}

	@Override
    public void execute(ScheduleStudySessionInputData inputData) {
        ScheduledSession session = new ScheduledSession(
                null,
                inputData.getStartTime(),
                inputData.getEndTime(),
                inputData.getTitle()
        );

        ScheduledSession savedSession = dataAccess.saveSession(inputData.getUserId(), session);
        ScheduleStudySessionOutputData outputData = new ScheduleStudySessionOutputData(savedSession);
        outputBoundary.prepareSuccessView(outputData);
    }
}
