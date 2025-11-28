package interface_adapter.controller;

import interface_adapter.view_model.QuizViewModel;
import use_case.generate_quiz.GenerateQuizInputBoundary;
import use_case.generate_quiz.GenerateQuizInputData;

import java.util.List;
import java.util.Objects;

/**
 * Controller for Generate Quiz.
 * Receives UI events and delegates to interactor and ViewModel.
 */
public class GenerateQuizController {

    private final GenerateQuizInputBoundary interactor;
    private final QuizViewModel viewModel;

    public GenerateQuizController(GenerateQuizInputBoundary interactor, QuizViewModel viewModel) {
        this.interactor = Objects.requireNonNull(interactor, "Interactor cannot be null");
        this.viewModel = Objects.requireNonNull(viewModel, "ViewModel cannot be null");
    }

    /**
     * Request quiz generation from interactor.
     */
    public void generateQuiz(String userId, String sessionId, String courseId,
                             String prompt, List<String> referenceMaterials) {
        if (userId == null || prompt == null) {
            throw new IllegalArgumentException("User ID and prompt cannot be null");
        }

        GenerateQuizInputData inputData = new GenerateQuizInputData(
                userId, sessionId, courseId, prompt, referenceMaterials
        );

        interactor.execute(inputData);
    }

    /**
     * Handle submit answer action from the View.
     * Delegates scoring and state update to the ViewModel.
     */
    public void submitAnswer() {
        viewModel.submitAnswer();
    }

    /**
     * Handle next question action from the View.
     * Delegates navigation to the ViewModel.
     */
    public void nextQuestion() {
        viewModel.nextQuestion();
    }
}
