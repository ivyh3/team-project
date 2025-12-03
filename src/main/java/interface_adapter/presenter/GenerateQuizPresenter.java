package interface_adapter.presenter;

import java.util.List;

import interface_adapter.view_model.AnswerableQuestion;
import interface_adapter.view_model.QuizState;
import interface_adapter.view_model.QuizViewModel;
import interface_adapter.view_model.ViewManagerModel;
import use_case.generate_quiz.GenerateQuizOutputBoundary;
import use_case.generate_quiz.GenerateQuizOutputData;

/**
 * Presenter for Generate Quiz.
 * Updates the QuizViewModel based on interactor output.
 */
public class GenerateQuizPresenter implements GenerateQuizOutputBoundary {

    private final QuizViewModel quizViewModel;
    private final ViewManagerModel viewManagerModel;

    public GenerateQuizPresenter(QuizViewModel quizViewModel, ViewManagerModel viewManagerModel) {
        this.quizViewModel = quizViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(GenerateQuizOutputData outputData) {
        List<AnswerableQuestion> questions = outputData.getQuestions();
        if (questions == null || questions.isEmpty()) {
            prepareFailView("No questions were generated.");
            return;
        }

        quizViewModel.setState(new QuizState(questions, outputData.getStartTime()));
        quizViewModel.firePropertyChange();
        viewManagerModel.setView(quizViewModel.getViewName());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // FUCK
        System.out.println("GenerateQuizPresenter preparing fail view: " + errorMessage);
        viewManagerModel.setView("dashboard");
    }
}