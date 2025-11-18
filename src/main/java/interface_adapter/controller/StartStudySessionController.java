package interface_adapter.controller;

import use_case.start_study_session.StartStudySessionInputBoundary;
import use_case.start_study_session.StartStudySessionInputData;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for the Start Study Session use case.
 */
public class StartStudySessionController {
    private final StartStudySessionInputBoundary interactor;
    
    public StartStudySessionController(StartStudySessionInputBoundary interactor) {
        this.interactor = interactor;
    }
    
    /**
     * Executes the start study session use case.
     * @param userId the user ID
     * @param courseId the course ID
     * @param startTime the start time
     * @param referenceMaterialIds the reference material IDs
     * @param prompt the study session prompt
     */
    public void execute(String userId, String courseId, LocalDateTime startTime, 
                       List<String> referenceMaterialIds, String prompt) {
        StartStudySessionInputData inputData = new StartStudySessionInputData(
            userId, courseId, startTime, referenceMaterialIds, prompt
        );
        interactor.execute(inputData);
    }
}
