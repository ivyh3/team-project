package use_case.schedule_study_session;

import entity.ScheduledSession;
import java.util.List;

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

        String formattedStartTime = session.getStartTime().toString();
        String formattedEndTime = session.getEndTime().toString();

        String sessionId = dataAccess.saveSession(inputData.getUserId(), session).getId();
        ScheduleStudySessionOutputData outputData = new ScheduleStudySessionOutputData(
                sessionId,
                session.getTitle(),
                formattedStartTime,
                formattedEndTime,
                true
        );
        outputBoundary.prepareSuccessView(outputData);
    }

    @Override
    public void delete(DeleteScheduledSessionInputData inputData) {

        ScheduledSession sessionToDelete = dataAccess.getScheduledSessionById(
                inputData.getUserId(), inputData.getSessionId());

        if (sessionToDelete != null) {

            dataAccess.deleteSession(inputData.getUserId(), sessionToDelete);

            DeleteScheduledSessionOutputData outputData = new DeleteScheduledSessionOutputData(
                    sessionToDelete.getId(),
                    "Session deleted successfully!"
            );
            outputBoundary.prepareDeleteSuccessView(outputData);
        } else {
            outputBoundary.prepareFailView("Session not found or already deleted.");
        }
    }
    @Override
    public void executeLoad(String userId) {
        List<ScheduledSession> sessions = dataAccess.getAllSessions(userId);
        outputBoundary.loadSessions(sessions);
    }
}
