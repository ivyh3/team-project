package interface_adapter.view_model;

import entity.Question;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for Study Quiz.
 * Exposes all quiz state to the View and notifies observers on change.
 * Presenter can update the ViewModel via public setters.
 */
public class QuizViewModel extends ViewModel<QuizState> {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private String currentQuestion = "";
    private List<String> currentOptions = new ArrayList<>();
    private String explanation = "";
    private String scoreDisplay = "0/0";
    private boolean submitEnabled = true;
    private boolean nextEnabled = false;
    private boolean quizComplete = false;

    private int selectedAnswer = -1;
    private int currentQuestionIndex = 0;
    private int totalQuestions = 0;
    private int score = 0;

    private List<Question> questions = new ArrayList<>();

    public QuizViewModel() {
        super("studyQuiz");
        setState(new QuizState());
    }

    // --- PropertyChangeListener support ---
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    // --- Public setters for Presenter ---
    public void setCurrentQuestion(String currentQuestion) {
        String old = this.currentQuestion;
        this.currentQuestion = currentQuestion != null ? currentQuestion : "";
        pcs.firePropertyChange("currentQuestion", old, this.currentQuestion);
    }

    public void setCurrentOptions(List<String> currentOptions) {
        List<String> old = this.currentOptions;
        this.currentOptions = currentOptions != null ? currentOptions : new ArrayList<>();
        pcs.firePropertyChange("currentOptions", old, this.currentOptions);
    }

    public void setExplanation(String explanation) {
        String old = this.explanation;
        this.explanation = explanation != null ? explanation : "";
        pcs.firePropertyChange("explanation", old, this.explanation);
    }

    public void setScoreDisplay(String scoreDisplay) {
        String old = this.scoreDisplay;
        this.scoreDisplay = scoreDisplay != null ? scoreDisplay : "0/0";
        pcs.firePropertyChange("scoreDisplay", old, this.scoreDisplay);
    }

    public void setSubmitEnabled(boolean submitEnabled) {
        boolean old = this.submitEnabled;
        this.submitEnabled = submitEnabled;
        pcs.firePropertyChange("submitEnabled", old, submitEnabled);
    }

    public void setNextEnabled(boolean nextEnabled) {
        boolean old = this.nextEnabled;
        this.nextEnabled = nextEnabled;
        pcs.firePropertyChange("nextEnabled", old, nextEnabled);
    }

    public void setQuizComplete(boolean quizComplete) {
        boolean old = this.quizComplete;
        this.quizComplete = quizComplete;
        pcs.firePropertyChange("quizComplete", old, quizComplete);
        setSubmitEnabled(!quizComplete);
        setNextEnabled(!quizComplete);
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions != null ? questions : new ArrayList<>();
        this.totalQuestions = this.questions.size();
        this.currentQuestionIndex = 0;
        this.score = 0;

        if (!this.questions.isEmpty()) {
            updateCurrentQuestion();
        }

        setScoreDisplay(score + "/" + totalQuestions);
        setSubmitEnabled(true);
        setNextEnabled(false);
    }

    // --- Internal logic ---
    private void updateCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            setCurrentQuestion(q.getText());
            setCurrentOptions(q.getOptions());
            setExplanation("");
            setSubmitEnabled(true);
            setNextEnabled(false);
        } else {
            setQuizComplete(true);
        }
    }

    // --- Selected answer ---
    public int getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(int selectedAnswer) {
        int old = this.selectedAnswer;
        this.selectedAnswer = selectedAnswer;
        pcs.firePropertyChange("selectedAnswer", old, selectedAnswer);
    }

    // --- Submission / scoring ---
    public void submitAnswer() {
        if (currentQuestionIndex >= questions.size() || selectedAnswer < 0) return;

        Question current = questions.get(currentQuestionIndex);
        if (selectedAnswer == current.getCorrectIndex()) score++;

        setExplanation(current.getExplanation());
        setScoreDisplay(score + "/" + totalQuestions);

        setSubmitEnabled(false);
        setNextEnabled(true);
    }

    public void nextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            selectedAnswer = -1;
            updateCurrentQuestion();
        } else {
            setQuizComplete(true);
        }
    }

    // --- Getters for View ---
    public String getCurrentQuestion() { return currentQuestion; }
    public List<String> getCurrentOptions() { return currentOptions; }
    public String getExplanation() { return explanation; }
    public String getScoreDisplay() { return scoreDisplay; }
    public boolean isSubmitEnabled() { return submitEnabled; }
    public boolean isNextEnabled() { return nextEnabled; }
    public boolean isQuizComplete() { return quizComplete; }
    public int getCurrentQuestionIndex() { return currentQuestionIndex; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getScore() { return score; }
}
