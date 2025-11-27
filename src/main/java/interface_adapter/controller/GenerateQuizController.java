package interface_adapter.controller;

import use_case.generate_quiz.GenerateQuizInputBoundary;
import use_case.generate_quiz.GenerateQuizInputData;

import java.util.List;
import java.util.Objects;

/**
 * Controller for the Generate Quiz use case.
 * Collects user input and delegates the quiz generation request to the interactor.
 */
public class GenerateQuizController {

    private final GenerateQuizInputBoundary interactor;

    /**
     * Constructs the controller with the given interactor.
     *
     * @param interactor the input boundary interactor
     */
    public GenerateQuizController(GenerateQuizInputBoundary interactor) {
        this.interactor = Objects.requireNonNull(interactor);
    }

    /**
     * Handles a request to generate a quiz.
     *
     * @param userId               the user ID (cannot be null)
     * @param sessionId            the session ID
     * @param courseId             the course ID
     * @param prompt               the prompt for quiz generation (cannot be null)
     * @param referenceMaterialIds the reference material IDs
     */
    public void generateQuiz(String userId, String sessionId, String courseId,
                             String prompt, List<String> referenceMaterialIds) {
        if (userId == null || prompt == null) {
            throw new IllegalArgumentException("User ID and prompt cannot be null");
        }

        GenerateQuizInputData inputData = new GenerateQuizInputData(
                userId, sessionId, courseId, prompt, referenceMaterialIds
        );

        interactor.execute(inputData);
    }
}
