package interface_adapter.presenter;

import entity.Question;
import interface_adapter.view_model.QuizViewModel;
import use_case.generate_quiz.GenerateQuizOutputBoundary;
import use_case.generate_quiz.GenerateQuizOutputData;

import java.util.List;

/**
 * Presenter for the Generate Quiz use case.
 * Formats output data and updates the QuizViewModel.
 */
public class GenerateQuizPresenter implements GenerateQuizOutputBoundary {
    private final QuizViewModel viewModel;

    public GenerateQuizPresenter(QuizViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(GenerateQuizOutputData outputData) {
        List<Question> questions = outputData.getQuestions();

        if (questions != null && !questions.isEmpty()) {
            // Update view model with first question
            Question firstQuestion = questions.get(0);
            viewModel.setCurrentQuestion(firstQuestion.getText());
            viewModel.setCurrentOptions(firstQuestion.getOptions());
            viewModel.setCurrentQuestionNumber(1);
            viewModel.setTotalQuestions(questions.size());
            viewModel.setScoreDisplay("0/" + questions.size());
            viewModel.setQuizComplete(false);
            viewModel.setShowingExplanation(false);
            viewModel.setErrorMessage("");
        } else {
            prepareFailView("No questions were generated.");
        }
    }

    public void GenerateQuiz(GenerateQuizOutputData outputData) {
        // Simply call prepareSuccessView
        prepareSuccessView(outputData);
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setErrorMessage(error);
        viewModel.setQuizComplete(false);
    }
}
