package use_case.review_quiz_history;

import interface_adapter.repository.StudyQuizRepository;

/**
 * Interactor for the Review Quiz History use case.
 */
public class ReviewQuizHistoryInteractor implements ReviewQuizHistoryInputBoundary {
    private final StudyQuizRepository quizRepository;
    private final ReviewQuizHistoryOutputBoundary outputBoundary;
    
    public ReviewQuizHistoryInteractor(StudyQuizRepository quizRepository,
                                      ReviewQuizHistoryOutputBoundary outputBoundary) {
        this.quizRepository = quizRepository;
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

