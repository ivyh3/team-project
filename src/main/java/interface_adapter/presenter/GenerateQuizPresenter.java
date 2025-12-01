package interface_adapter.presenter;

import entity.Question;
import interface_adapter.view_model.QuizViewModel;
import use_case.generate_quiz.GenerateQuizOutputBoundary;
import use_case.generate_quiz.GenerateQuizOutputData;

import java.util.List;

/**
 * Presenter for Generate Quiz.
 * Updates the QuizViewModel based on interactor output.
 */
public class GenerateQuizPresenter implements GenerateQuizOutputBoundary {

    private final QuizViewModel quizViewModel;

    public GenerateQuizPresenter(QuizViewModel quizViewModel) {
        this.quizViewModel = quizViewModel;
    }

    @Override
    public void prepareSuccessView(GenerateQuizOutputData outputData) {
        List<Question> questions = outputData.getQuestions();
        if (questions == null || questions.isEmpty()) {
            prepareFailView("No questions were generated.");
            return;
        }

        quizViewModel.setQuestions(questions);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        quizViewModel.setQuestions(List.of()); // reset quiz to empty
    }
}