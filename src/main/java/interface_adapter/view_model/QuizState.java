package interface_adapter.view_model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuizState {
    private LocalDateTime startTime;
    private List<AnswerableQuestion> questions;
    private int currentQuestionIndex;
    private String errorMessage;

    public QuizState(List<AnswerableQuestion> questions, LocalDateTime startTime) {
        this.questions = questions;
        this.startTime = startTime;
        this.currentQuestionIndex = 0;
        this.errorMessage = null;
    }

    public List<AnswerableQuestion> getQuestions() {
        return questions;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public AnswerableQuestion getCurrentQuestion() {
        if (questions.isEmpty() || currentQuestionIndex >= questions.size())
            return null;
        return questions.get(currentQuestionIndex);
    }

    public int getNumCorrect() {
        return questions.stream().filter(AnswerableQuestion::isAnsweredCorrectly).toList().size();
    }

    public int getNumQuestions() {
        return questions.size();
    }

    public void setQuestions(List<AnswerableQuestion> questions) {
        this.questions = questions;
        reset();
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    private void reset() {
        currentQuestionIndex = 0;
        questions = new ArrayList<>(questions);
    }

    public void nextQuestion() {
        if (currentQuestionIndex < questions.size())
            currentQuestionIndex++;
    }

    public boolean isQuizComplete() {
        return currentQuestionIndex >= questions.size();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void submitAnswer(int selectedIndex) {
        AnswerableQuestion current = getCurrentQuestion();
        if (current == null)
            return;

        current.setChosenIndex(selectedIndex);

        // Violates SRP but makes life easier.
        // AND I WANT LIFE TO BE EASEIER
        nextQuestion();
    }
}
