package interface_adapter.controller;

import use_case.review_quiz_history.ReviewQuizHistoryInputBoundary;
import use_case.review_quiz_history.ReviewQuizHistoryInputData;

import java.util.Objects;

/**
 * Controller for the Review Quiz History use case.
 * Collects user input and delegates the request to the interactor.
 */
public class ReviewQuizHistoryController {

    private final ReviewQuizHistoryInputBoundary interactor;

    /**
     * Constructs the controller with the given interactor.
     *
     * @param interactor the input boundary interactor
     */
    public ReviewQuizHistoryController(ReviewQuizHistoryInputBoundary interactor) {
        this.interactor = Objects.requireNonNull(interactor, "Interactor cannot be null");
    }

    /**
     * Handles a request to review a user's quiz history.
     *
     * @param userId   the user ID (cannot be null)
     * @param courseId the course ID (optional, null to review all courses)
     */
    public void reviewQuizHistory(String userId, String courseId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        ReviewQuizHistoryInputData inputData = new ReviewQuizHistoryInputData(userId, courseId);
        interactor.execute(inputData);
    }
}
