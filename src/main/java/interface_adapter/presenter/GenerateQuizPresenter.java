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

        // Populate the ViewModel with questions
        quizViewModel.setQuestions(questions);
        quizViewModel.setQuizComplete(false);
        quizViewModel.setExplanation("");
        quizViewModel.setScoreDisplay("0/" + questions.size());
        quizViewModel.setSubmitEnabled(true);
        quizViewModel.setNextEnabled(false);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // Clear previous state and display error
        quizViewModel.setQuestions(List.of());
        quizViewModel.setCurrentQuestion("");
        quizViewModel.setCurrentOptions(List.of());
        quizViewModel.setExplanation(errorMessage);
        quizViewModel.setScoreDisplay("0/0");
        quizViewModel.setQuizComplete(true);
    }
}
