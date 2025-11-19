package use_case.review_quiz_history;

/**
 * Interactor for the Review Quiz History use case.
 */
public class ReviewQuizHistoryInteractor implements ReviewQuizHistoryInputBoundary {
	private final ReviewQuizHistoryOutputBoundary outputBoundary;

	public ReviewQuizHistoryInteractor(ReviewQuizHistoryOutputBoundary outputBoundary) {
		this.outputBoundary = outputBoundary;
	}

	@Override
	public void execute(ReviewQuizHistoryInputData inputData) {
		// TODO: Implement the business logic for reviewing quiz history
		// 1. Fetch quizzes from repository for the user and course
		// 2. Sort by date
		// 3. Prepare success or failure view
	}
}
