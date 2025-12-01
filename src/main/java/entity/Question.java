// In Question.java
package entity;

import java.util.List;

public class Question {
    private final String id;
    private final String text;
    private final List<String> options;
    private final int correctIndex;
    private final String explanation;

    private boolean wasAnswered = false; // track if question was answered
    private boolean wasCorrect = false;  // track if question was answered correctly

    public Question(String id, String text, List<String> options, int correctIndex, String explanation) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
    }

    // Existing getters
    public String getId() { return id; }
    public String getText() { return text; }
    public List<String> getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
    public String getExplanation() { return explanation; }

    // New getter/setter for answered state
    public boolean isAnswered() { return wasAnswered; }
    public void setAnswered(boolean answered) { this.wasAnswered = answered; }

    // Getter/setter for correctness state
    public boolean isCorrect() { return wasCorrect; }
    public void setCorrect(boolean correct) { this.wasCorrect = correct; }

}