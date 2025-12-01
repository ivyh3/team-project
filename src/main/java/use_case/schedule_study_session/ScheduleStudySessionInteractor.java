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

        dataAccess.saveSession(inputData.getUserId(), session);
        ScheduleStudySessionOutputData outputData = new ScheduleStudySessionOutputData(session);
        outputBoundary.prepareSuccessView(outputData);
    }

    @Override
    public void delete(DeleteScheduledSessionInputData inputData) {

        ScheduledSession sessionToDelete = dataAccess.getScheduledSessionById(
                inputData.getUserId(), inputData.getSessionId());

        if (sessionToDelete != null) {

            dataAccess.deleteSession(inputData.getUserId(), sessionToDelete); // Calls the DAO delete method

            DeleteScheduledSessionOutputData outputData = new DeleteScheduledSessionOutputData(
                    sessionToDelete.getId(),
                    "Session deleted successfully!"
            );
            outputBoundary.prepareDeleteSuccessView(outputData);
        } else {
            outputBoundary.prepareFailView("Session not found or already deleted.");
        }
    }
}
