package interface_adapter.view_model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Question;

import java.util.ArrayList;
import java.util.List;

public class QuizViewModel {

    private final GeminiClient client;
    private String prompt;
    private String referenceText;
    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;

    public QuizViewModel(GeminiClient client) {
        this.client = client;
    }

    // ----------------- getters -----------------
    public List<Question> getQuestions() { return questions; }
    public int getCurrentQuestionIndex() { return currentQuestionIndex; }
    public Question getCurrentQuestion() {
        if (questions.isEmpty() || currentQuestionIndex >= questions.size()) return null;
        return questions.get(currentQuestionIndex);
    }
    public int getScore() { return score; }
    public int getAnsweredCount() {
        int count = 0;
        for (Question q : questions) {
            if (q.isAnswered()) count++;
        }
        return count;
    }

    // ----------------- setters -----------------
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public void setReferenceText(String referenceText) { this.referenceText = referenceText; }
    public void reset() {
        currentQuestionIndex = 0;
        score = 0;
        if (questions != null) {
            for (Question q : questions) {
                q.setAnswered(false);
            }
        }
    }
    public void nextQuestion() { if (currentQuestionIndex < questions.size()) currentQuestionIndex++; }

    // Update generateQuizFromGemini
    public void generateQuizFromGemini() throws Exception {
        if (prompt == null || referenceText == null)
            throw new IllegalStateException("Prompt/reference missing");

        String rawJson = client.generateQuestions(prompt, referenceText);
        System.out.println("Gemini raw JSON: " + rawJson);

        this.questions = parseGeminiResponse(rawJson); // parse first
        reset(); // reset after questions are populated

        System.out.println("Parsed questions count: " + questions.size());
    }

    private List<Question> parseGeminiResponse(String response) throws Exception {
        List<Question> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Raw Gemini response: " + response);
        JsonNode root = mapper.readTree(response);

        JsonNode candidatesNode = root.path("candidates");
        if (candidatesNode.isMissingNode() || !candidatesNode.isArray()) return result;

        for (JsonNode candidate : candidatesNode) {
            JsonNode textNode = candidate.path("content").path("parts").get(0).path("text");
            if (textNode.isMissingNode()) continue;

            String[] blocks = textNode.asText().split("\\n\\n");
            for (String block : blocks) {
                if (!block.contains("A)") || !block.contains("Answer:")) continue;

                String[] lines = block.split("\\n");
                String qText = lines[0].replaceFirst("^\\d+\\.\\s*", "").trim();
                List<String> choices = new ArrayList<>();
                for (int i = 1; i <= 4; i++) choices.add(lines[i].substring(3).trim());

                if (lines.length < 6) continue;
                String answerLine = lines[5].replace("Answer:", "").trim();
                String letter = answerLine.substring(0, 1).toUpperCase();

                int correctIndex;
                switch (letter) {
                    case "A": correctIndex = 0; break;
                    case "B": correctIndex = 1; break;
                    case "C": correctIndex = 2; break;
                    case "D": correctIndex = 3; break;
                    default: correctIndex = -1;
                }

                result.add(new Question("Q" + (result.size() + 1), qText, choices, correctIndex, ""));
            }
        }
        System.out.println("Parsed questions: " + result.size());
        return result;
    }

    // ----------------- answer submission -----------------
    public void submitAnswer(int selectedIndex) {
        Question current = getCurrentQuestion();
        if (current == null) return;

        boolean correct = selectedIndex == current.getCorrectIndex();
        current.setAnswered(true);
        if (correct) score++;
    }

    // ----------------- load from PDF fallback -----------------
    public void loadQuizFromText(String pdfText) {
        questions.clear();
        String[] lines = pdfText.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (!lines[i].isBlank()) {
                questions.add(new Question(
                        "Q" + (i + 1),
                        lines[i],
                        List.of("A", "B", "C", "D"), // placeholder choices
                        0,
                        ""
                ));
            }
        }
        reset();
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        reset();
    }

    // Add field to store the last Gemini raw JSON
    private String lastRawJson;

    public String getLastRawJson() {
        return lastRawJson;
    }

    public void submitAnswer(int questionIndex, int selectedIndex) {
        if (questionIndex < 0 || questionIndex >= questions.size()) return;

        Question question = questions.get(questionIndex);
        boolean correct = selectedIndex == question.getCorrectIndex();
        question.setAnswered(true);
        if (correct) score++;
    }
}