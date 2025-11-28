package interface_adapter.view_model;

import java.util.Collections;
import java.util.List;

public final class QuizState {

    private final String questionText;
    private final List<String> answerOptions;
    private final int questionNumber;
    private final int totalQuestions;
    private final String explanation;
    private final int score;
    private final boolean showingExplanation;
    private final boolean quizComplete;
    private final String errorMessage;

    // No-argument constructor with default values
    public QuizState() {
        this.questionText = "";
        this.answerOptions = Collections.emptyList();
        this.questionNumber = 0;
        this.totalQuestions = 0;
        this.explanation = "";
        this.score = 0;
        this.showingExplanation = false;
        this.quizComplete = false;
        this.errorMessage = "";
    }

    // Getters only — immutable
    public String getQuestionText() { return questionText; }
    public List<String> getAnswerOptions() { return answerOptions; }
    public int getQuestionNumber() { return questionNumber; }
    public int getTotalQuestions() { return totalQuestions; }
    public String getExplanation() { return explanation; }
    public int getScore() { return score; }
    public boolean isShowingExplanation() { return showingExplanation; }
    public boolean isQuizComplete() { return quizComplete; }
    public String getErrorMessage() { return errorMessage; }
}
