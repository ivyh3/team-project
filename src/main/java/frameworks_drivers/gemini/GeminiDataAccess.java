package frameworks_drivers.gemini;

import entity.Question;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * GeminiDataAccess implements a DAO for generating quizzes.
 * It simulates API calls to Gemini and parses the response into Question objects.
 */
public class GeminiDataAccess {

    /**
     * Generates quiz questions based on a prompt and reference text.
     *
     * @param prompt             The quiz prompt.
     * @param referenceMaterials List of reference material strings.
     * @return List of Question objects.
     */
    public List<Question> generateQuiz(String prompt, List<String> referenceMaterials) {
        Objects.requireNonNull(prompt, "Prompt cannot be null");
        Objects.requireNonNull(referenceMaterials, "Reference materials cannot be null");

        String referenceText = String.join("\n\n", referenceMaterials);

        // Simulated API call
        String rawJson = simulateGeminiApi(prompt, referenceText);

        try {
            return parseGeminiResponse(rawJson);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Simulates a Gemini API call.
     *
     * @param prompt        The quiz prompt.
     * @param referenceText Reference text.
     * @return Simulated JSON string response.
     */
    private String simulateGeminiApi(String prompt, String referenceText) {
        // Simple static example
        return "{ \"candidates\": [ { \"content\": { \"parts\": [ {\"text\": \"1. What is 2+2?\\nA) 3\\nB) 4\\nC) 5\\nD) 6\\nAnswer: B\"} ] } } ] }";
    }

    /**
     * Parses Gemini JSON response into a list of Question objects.
     *
     * @param response JSON string from Gemini.
     * @return List of parsed Question objects.
     * @throws Exception If parsing fails.
     */
    private List<Question> parseGeminiResponse(String response) throws Exception {
        List<Question> questions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);

        JsonNode candidates = root.path("candidates");
        if (!candidates.isArray()) return questions;

        for (JsonNode candidate : candidates) {
            JsonNode parts = candidate.path("content").path("parts");
            if (!parts.isArray() || parts.size() == 0) continue;

            JsonNode textNode = parts.get(0).path("text");
            if (textNode.isMissingNode()) continue;

            String[] lines = textNode.asText().split("\\n");
            if (lines.length < 6) continue;

            String qText = lines[0].replaceFirst("^\\d+\\.\\s*", "").trim();
            List<String> choices = new ArrayList<>();
            for (int i = 1; i <= 4; i++) {
                if (lines[i].length() > 3) {
                    choices.add(lines[i].substring(3).trim());
                } else {
                    choices.add(lines[i].trim());
                }
            }

            String answerLetter = lines[5].replace("Answer:", "").trim();
            int correctIndex = -1;
            if (!answerLetter.isEmpty()) {
                switch (answerLetter.toUpperCase().charAt(0)) {
                    case 'A': correctIndex = 0; break;
                    case 'B': correctIndex = 1; break;
                    case 'C': correctIndex = 2; break;
                    case 'D': correctIndex = 3; break;
                }
            }

            questions.add(new Question("Q" + (questions.size() + 1), qText, choices, correctIndex, ""));
        }

        return questions;
    }
}