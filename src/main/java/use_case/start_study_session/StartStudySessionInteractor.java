package use_case.start_study_session;

import entity.StudySession;
import interface_adapter.repository.StudySessionRepository;

/**
 * Interactor for the Start Study Session use case.
 */
public class StartStudySessionInteractor implements StartStudySessionInputBoundary {
    private final StudySessionRepository studySessionRepository;
    private final StartStudySessionOutputBoundary outputBoundary;
    
    public StartStudySessionInteractor(StudySessionRepository studySessionRepository,
                                      StartStudySessionOutputBoundary outputBoundary) {
        this.studySessionRepository = studySessionRepository;
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

