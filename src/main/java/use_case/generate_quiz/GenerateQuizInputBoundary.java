package use_case.generate_quiz;

/**
 * Input boundary for the Generate Quiz use case.
 * Implementations handle quiz generation logic.
 */
public interface GenerateQuizInputBoundary {
    /**
     * Executes the generate quiz use case.
     *
     * @param inputData the input data for generating a quiz
     * @throws IllegalArgumentException if inputData is invalid
     */
    void execute(GenerateQuizInputData inputData);
}
