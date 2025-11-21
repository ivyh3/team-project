package use_case.generate_quiz;

/**
 * Output boundary for the Generate Quiz use case.
 */
public interface GenerateQuizOutputBoundary {
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
