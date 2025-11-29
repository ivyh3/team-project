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

    public QuizState(
            String questionText,
            List<String> answerOptions,
            int questionNumber,
            int totalQuestions,
            String explanation,
            int score,
            boolean showingExplanation,
            boolean quizComplete,
            String errorMessage
    ) {
        this.questionText = questionText;
        this.answerOptions = List.copyOf(answerOptions);
        this.questionNumber = questionNumber;
        this.totalQuestions = totalQuestions;
        this.explanation = explanation;
        this.score = score;
        this.showingExplanation = showingExplanation;
        this.quizComplete = quizComplete;
        this.errorMessage = errorMessage;
    }

    public QuizState() {
        this("", Collections.emptyList(), 0, 0, "", 0, false, false, "");
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
