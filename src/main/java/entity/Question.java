package entity;

import java.util.List;

public class Question {
    private final String question;
    private final List<String> options;
    private final int correctIndex;
    private final String explanation;

    public Question(String question, List<String> options, int correctIndex, String explanation) {
        this.question = question;
        this.options = options;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
    }

    // Existing getters
    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public String getExplanation() {
        return explanation;
    }

    @Override
    public String toString() {
        return "Question{"
                + "question='" + question + '\''
                + ", options=" + options
                + ", correctIndex=" + correctIndex
                + ", explanation='" + explanation + '\''
                + '}';
    }
}
