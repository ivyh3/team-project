package use_case.generate_quiz;

/**
 * Input boundary for the Generate Quiz use case.
 */
public interface GenerateQuizInputBoundary {
	/**
	 * Executes the generate quiz use case.
	 * 
	 * @param inputData the input data for generating a quiz
	 */
	void execute(GenerateQuizInputData inputData);
}
