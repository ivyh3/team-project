package interface_adapter.view_model;

import java.util.List;

public class AnswerableQuestion {
    private final String questionText;
    private final List<String> choices;
    private final int correctIndex;
    private final String explanation;
    private int chosenIndex = -1; // -1 indicates no choice made yet

    public AnswerableQuestion(String questionText, List<String> choices, int correctIndex, String explanation) {
        this.questionText = questionText;
        this.choices = choices;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getChoices() {
        return choices;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public int getChosenIndex() {
        return chosenIndex;
    }

    public boolean isAnswered() {
        return chosenIndex != -1;
    }

    public boolean isAnsweredCorrectly() {
        return chosenIndex == correctIndex;
    }

    public int setChosenIndex(int chosenIndex) {
        return this.chosenIndex = chosenIndex;
    }
}
