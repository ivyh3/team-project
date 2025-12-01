package use_case.generate_quiz;

/**
 * Output boundary for the Generate Quiz use case.
 * Responsible for presenting the results to the presenter.
 */
public interface GenerateQuizOutputBoundary {

    /**
     * Prepares the success view with the generated quiz data.
     *
     * @param outputData the output data containing questions and metadata
     */
    void prepareSuccessView(GenerateQuizOutputData outputData);

    /**
     * Prepares the failure view with an error message.
     *
     * @param error the error message to display
     */
    void prepareFailView(String error);
}
