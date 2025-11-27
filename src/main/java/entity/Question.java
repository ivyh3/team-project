package entity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Simple Question model used for mapping Gemini response JSON.
 */
public class Question {

    private final String id;
    private final String questionText;
    private final List<String> choices;
    private final int correctIndex;
    private final String explanation;
    private boolean wasCorrect; // NEW: tracks if user answered correctly

    public Question(String id, String questionText, List<String> choices, int correctIndex, String explanation) {
        this.id = Objects.requireNonNull(id);
        this.questionText = Objects.requireNonNull(questionText);
        this.choices = choices == null ? Collections.emptyList() : List.copyOf(choices);
        this.correctIndex = correctIndex;
        this.explanation = explanation == null ? "" : explanation;
        this.wasCorrect = false; // default
    }

    public String getId() { return id; }

    public String getText() { return questionText; }

    public List<String> getOptions() { return choices; }

    public int getCorrectIndex() { return correctIndex; }

    public String getExplanation() { return explanation; }

    // --- NEW ---
    public boolean isWasCorrect() { return wasCorrect; }

    public void setWasCorrect(boolean wasCorrect) { this.wasCorrect = wasCorrect; }
}
