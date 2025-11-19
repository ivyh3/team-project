package use_case.generate_quiz;

import frameworks_drivers.gemini.GeminiService;

/**
 * Interactor for the Generate Quiz use case.
 */
public class GenerateQuizInteractor implements GenerateQuizInputBoundary {
	private final GeminiService geminiService;
	private final GenerateQuizOutputBoundary outputBoundary;

	public GenerateQuizInteractor(GeminiService geminiService,
			GenerateQuizOutputBoundary outputBoundary) {
		this.geminiService = geminiService;
		this.outputBoundary = outputBoundary;
	}

	@Override
	public void execute(GenerateQuizInputData inputData) {
		// TODO: Implement the business logic for generating a quiz
		// 1. Fetch reference materials
		// 2. Call Gemini API to generate quiz questions
		// 3. Create StudyQuiz entity
		// 4. Save to repository
		// 5. Prepare success or failure view
	}
}
