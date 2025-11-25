package use_case.generate_quiz;

/**
 * Output boundary for the Generate Quiz use case.
 */
public interface GenerateQuizOutputBoundary {
    /**
     * Generate the quiz.
     * @param outputData The output data.
     */
    void GenerateQuiz(GenerateQuizOutputData outputData);

    /**
     * Prepares the success view.
     * 
     * @param outputData the output data
     */
    void prepareSuccessView(GenerateQuizOutputData outputData);

    /**
     * Prepares the failure view.
     * 
     * @param error the error message
     */
    void prepareFailView(String error);
}
