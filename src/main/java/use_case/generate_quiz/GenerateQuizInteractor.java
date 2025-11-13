package use_case.generate_quiz;

import interface_adapter.repository.StudyQuizRepository;
import frameworks_drivers.gemini.GeminiService;

/**
 * Interactor for the Generate Quiz use case.
 */
public class GenerateQuizInteractor implements GenerateQuizInputBoundary {
	private final StudyQuizRepository quizRepository;
	private final GeminiService geminiService;
	private final GenerateQuizOutputBoundary outputBoundary;

	public GenerateQuizInteractor(StudyQuizRepository quizRepository,
			GeminiService geminiService,
			GenerateQuizOutputBoundary outputBoundary) {
		this.quizRepository = quizRepository;
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
