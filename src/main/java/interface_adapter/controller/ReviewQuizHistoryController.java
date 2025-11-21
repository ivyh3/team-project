package interface_adapter.controller;

import use_case.review_quiz_history.ReviewQuizHistoryInputBoundary;
import use_case.review_quiz_history.ReviewQuizHistoryInputData;

/**
 * Controller for the Review Quiz History use case.
 */
public class ReviewQuizHistoryController {
    private final ReviewQuizHistoryInputBoundary interactor;

    public ReviewQuizHistoryController(ReviewQuizHistoryInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the review quiz history use case.
     * 
     * @param userId   the user ID
     * @param courseId the course ID (optional, can be null for all courses)
     */
    public void execute(String userId, String courseId) {
        ReviewQuizHistoryInputData inputData = new ReviewQuizHistoryInputData(userId, courseId);
        interactor.execute(inputData);
    }
}
