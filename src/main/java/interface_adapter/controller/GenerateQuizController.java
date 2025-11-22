package interface_adapter.controller;

import use_case.generate_quiz.GenerateQuizInputBoundary;
import use_case.generate_quiz.GenerateQuizInputData;

import java.util.List;

/**
 * Controller for the Generate Quiz use case.
 */
public class GenerateQuizController {
    private final GenerateQuizInputBoundary interactor;

    public GenerateQuizController(GenerateQuizInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the generate quiz use case.
     * 
     * @param userId               the user ID
     * @param sessionId            the session ID
     * @param courseId             the course ID
     * @param prompt               the prompt for quiz generation
     * @param referenceMaterialIds the reference material IDs
     */
    public void execute(String userId, String sessionId, String courseId,
            String prompt, List<String> referenceMaterialIds) {
        GenerateQuizInputData inputData = new GenerateQuizInputData(
                userId, sessionId, courseId, prompt, referenceMaterialIds);
        interactor.execute(inputData);
    }
}
