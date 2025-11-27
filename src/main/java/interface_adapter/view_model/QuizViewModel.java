package interface_adapter.view_model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ViewModel for the Quiz view.
 * Stores the state and data that the quiz view needs to display.
 */
public class QuizViewModel {
    private final PropertyChangeSupport support;

    private String currentQuestion;
    private List<String> currentOptions;
    private int currentQuestionNumber;
    private int totalQuestions;
    private String explanation;
    private String scoreDisplay;
    private boolean quizComplete;
    private boolean showingExplanation;
    private String errorMessage;

    public QuizViewModel() {
        this.support = new PropertyChangeSupport(this);
        this.currentQuestion = "";
        this.currentOptions = new ArrayList<>();
        this.currentQuestionNumber = 0;
        this.totalQuestions = 0;
        this.explanation = "";
        this.scoreDisplay = "0/0";
        this.quizComplete = false;
        this.showingExplanation = false;
        this.errorMessage = "";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public String getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(String currentQuestion) {
        if (!Objects.equals(this.currentQuestion, currentQuestion)) {
            String oldValue = this.currentQuestion;
            this.currentQuestion = currentQuestion;
            support.firePropertyChange("currentQuestion", oldValue, currentQuestion);
        }
    }

    public List<String> getCurrentOptions() {
        return new ArrayList<>(currentOptions);
    }

    public void setCurrentOptions(List<String> currentOptions) {
        List<String> safeOptions = (currentOptions != null) ? new ArrayList<>(currentOptions) : new ArrayList<>();
        if (!safeOptions.equals(this.currentOptions)) {
            List<String> oldValue = this.currentOptions;
            this.currentOptions = safeOptions;
            support.firePropertyChange("currentOptions", oldValue, this.currentOptions);
        }
    }

    public int getCurrentQuestionNumber() {
        return currentQuestionNumber;
    }

    public void setCurrentQuestionNumber(int currentQuestionNumber) {
        if (this.currentQuestionNumber != currentQuestionNumber) {
            int oldValue = this.currentQuestionNumber;
            this.currentQuestionNumber = currentQuestionNumber;
            support.firePropertyChange("currentQuestionNumber", oldValue, currentQuestionNumber);
        }
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        if (this.totalQuestions != totalQuestions) {
            int oldValue = this.totalQuestions;
            this.totalQuestions = totalQuestions;
            support.firePropertyChange("totalQuestions", oldValue, totalQuestions);
        }
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        if (!Objects.equals(this.explanation, explanation)) {
            String oldValue = this.explanation;
            this.explanation = explanation;
            support.firePropertyChange("explanation", oldValue, explanation);
        }
    }

    public String getScoreDisplay() {
        return scoreDisplay;
    }

    public void setScoreDisplay(String scoreDisplay) {
        if (!Objects.equals(this.scoreDisplay, scoreDisplay)) {
            String oldValue = this.scoreDisplay;
            this.scoreDisplay = scoreDisplay;
            support.firePropertyChange("scoreDisplay", oldValue, scoreDisplay);
        }
    }

    public boolean isQuizComplete() {
        return quizComplete;
    }

    public void setQuizComplete(boolean quizComplete) {
        if (this.quizComplete != quizComplete) {
            boolean oldValue = this.quizComplete;
            this.quizComplete = quizComplete;
            support.firePropertyChange("quizComplete", oldValue, quizComplete);
        }
    }

    public boolean isShowingExplanation() {
        return showingExplanation;
    }

    public void setShowingExplanation(boolean showingExplanation) {
        if (this.showingExplanation != showingExplanation) {
            boolean oldValue = this.showingExplanation;
            this.showingExplanation = showingExplanation;
            support.firePropertyChange("showingExplanation", oldValue, showingExplanation);
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        if (!Objects.equals(this.errorMessage, errorMessage)) {
            String oldValue = this.errorMessage;
            this.errorMessage = errorMessage;
            support.firePropertyChange("errorMessage", oldValue, errorMessage);
        }
    }
}
