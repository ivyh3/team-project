package use_case.start_study_session;

import entity.StudySession;

/**
 * Interactor for the Start Study Session use case.
 */
public class StartStudySessionInteractor implements StartStudySessionInputBoundary {
	private final StartStudySessionOutputBoundary outputBoundary;

	public StartStudySessionInteractor(StartStudySessionOutputBoundary outputBoundary) {
		this.outputBoundary = outputBoundary;
	}

	@Override
	public void execute(StartStudySessionInputData inputData) {
		// TODO: Implement the business logic for starting a study session
		// 1. Create a new StudySession entity
		// 2. Save it to the repository
		// 3. Prepare success or failure view
	}
}
