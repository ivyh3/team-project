package use_case.review_quiz_history;

/**
 * Input boundary for the Review Quiz History use case.
 */
public interface ReviewQuizHistoryInputBoundary {
    /**
     * Executes the review quiz history use case.
     * 
     * @param inputData the input data for reviewing quiz history
     */
    void execute(ReviewQuizHistoryInputData inputData);
}
