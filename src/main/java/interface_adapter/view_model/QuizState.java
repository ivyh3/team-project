package interface_adapter.view_model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public QuizState(String questionText,
                     List<String> answerOptions,
                     int questionNumber,
                     int totalQuestions,
                     String explanation,
                     int score,
                     boolean showingExplanation,
                     boolean quizComplete,
                     String errorMessage) {

        this.questionText = Objects.requireNonNullElse(questionText, "");
        this.answerOptions = answerOptions == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(answerOptions);
        this.questionNumber = questionNumber;
        this.totalQuestions = totalQuestions;
        this.explanation = Objects.requireNonNullElse(explanation, "");
        this.score = score;
        this.showingExplanation = showingExplanation;
        this.quizComplete = quizComplete;
        this.errorMessage = Objects.requireNonNullElse(errorMessage, "");
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

    public static QuizState initial() {
        return new QuizState("", Collections.emptyList(), 0, 0, "",
                0, false, false, "");
    }
}
