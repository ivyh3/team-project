package use_case.review_quiz_history;

/**
 * Output boundary for the Review Quiz History use case.
 */
public interface ReviewQuizHistoryOutputBoundary {
    /**
     * Prepares the success view.
     * 
     * @param outputData the output data
     */
    void prepareSuccessView(ReviewQuizHistoryOutputData outputData);

    /**
     * Prepares the failure view.
     * 
     * @param error the error message
     */
    void prepareFailView(String error);
}
