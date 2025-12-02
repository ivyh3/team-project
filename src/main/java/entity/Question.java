package entity;

import java.util.List;

/**
 * Represents a single question in a quiz.
 */
public class Question {
    private String id;
    private String question;
    private List<String> possibleAnswers;
    private int chosenAnswer;
    private int correctAnswer;
    private boolean wasCorrect;
    private String explanation;

    public Question(String id, String question, List<String> possibleAnswers,
            int correctAnswer, String explanation) {
        this.id = id;
        this.question = question;
        this.possibleAnswers = possibleAnswers;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
        this.chosenAnswer = -1;
        this.wasCorrect = false;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(List<String> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    public int getChosenAnswer() {
        return chosenAnswer;
    }

    /**
     * Sets the user's answer and checks whether it is correct.
     *
    * @param chosenAnswer the answer selected by the user */
    public void setChosenAnswer(int chosenAnswer) {
        this.chosenAnswer = chosenAnswer;
        this.wasCorrect = chosenAnswer == correctAnswer;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean isWasCorrect() {
        return wasCorrect;
    }

    public void setWasCorrect(boolean wasCorrect) {
        this.wasCorrect = wasCorrect;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
