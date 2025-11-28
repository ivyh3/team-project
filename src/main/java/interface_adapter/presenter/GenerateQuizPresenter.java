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

    private final QuizViewModel viewModel;

    public GenerateQuizPresenter(QuizViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(GenerateQuizOutputData outputData) {
        List<Question> questions = outputData.getQuestions();
        if (questions == null || questions.isEmpty()) {
            prepareFailView("No questions were generated.");
            return;
        }

        // Populate the ViewModel with questions
        viewModel.setQuestions(questions);
        viewModel.setQuizComplete(false);
        viewModel.setExplanation("");
        viewModel.setScoreDisplay("0/" + questions.size());
        viewModel.setSubmitEnabled(true);
        viewModel.setNextEnabled(false);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // Clear previous state and display error
        viewModel.setQuestions(List.of());
        viewModel.setCurrentQuestion("");
        viewModel.setCurrentOptions(List.of());
        viewModel.setExplanation(errorMessage);
        viewModel.setScoreDisplay("0/0");
        viewModel.setQuizComplete(true);
    }
}
