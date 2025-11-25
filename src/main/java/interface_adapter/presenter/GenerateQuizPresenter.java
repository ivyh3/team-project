package interface_adapter.presenter;

import entity.Question;
import entity.StudyQuiz;
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
        StudyQuiz quiz = outputData.getQuiz();
        List<Question> questions = quiz.getQuestions();

        if (questions != null && !questions.isEmpty()) {
            // Update view model with first question
            Question firstQuestion = questions.getFirst();
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

    // Implement the interface method required by GenerateQuizOutputBoundary
    @Override
    public void GenerateQuiz(GenerateQuizOutputData outputData) {
        prepareSuccessView(outputData);
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setErrorMessage(error);
        viewModel.setQuizComplete(false);
    }
}
