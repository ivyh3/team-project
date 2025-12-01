package entity;

import java.util.List;

/**
 * Represents a multiple-choice question.
 */
public class Question {

    private final String id;
    private final String text;
    private final List<String> options;
    private final int correctIndex;
    private final String explanation;

    private boolean answered = false;
    private int chosenIndex = -1;

    public Question(String id, String text, List<String> options, int correctIndex, String explanation) {
        if (options == null || options.size() < 2)
            throw new IllegalArgumentException("Options must contain at least 2 choices.");
        if (correctIndex < 0 || correctIndex >= options.size())
            throw new IllegalArgumentException("Correct index out of bounds.");

        this.id = id;
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
    }

    public String getId() { return id; }
    public String getText() { return text; }
    public List<String> getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
    public String getExplanation() { return explanation; }

    public boolean isAnswered() { return answered; }
    public void setAnswered(boolean answered) { this.answered = answered; }

    public int getChosenIndex() { return chosenIndex; }
    public void setChosenIndex(int chosenIndex) { this.chosenIndex = chosenIndex; }

    public boolean isCorrect() {
        return answered && chosenIndex == correctIndex;
    }
}