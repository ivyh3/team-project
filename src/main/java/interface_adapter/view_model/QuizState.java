package interface_adapter.view_model;

import entity.Question;

import java.util.Collections;
import java.util.List;

public class QuizState {

    private final Question question;
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
            Question question,
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
        this.question = question;
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

    // Getters only — immutable
    public Question getQuestion() {
        return question;
    }
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
