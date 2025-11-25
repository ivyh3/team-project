package entity;

import java.util.List;

/**
 * Simple Question model used for mapping Gemini response JSON.
 * Fields must match the response JSON structure used in data/gemini/responses/*.json
 */
public class Question {
    private String id;
    private String text;
    private List<String> options;
    private String correctAnswer;
    private String explanation;

    public Question() {}

    public Question(String id, String text, List<String> options, String correctAnswer, String explanation) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}
