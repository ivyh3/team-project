package frameworks_drivers.gemini;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Question;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for generating quizzes via Gemini API.
 */
public class GeminiDataAccessObject {

    /**
     * Generates quiz questions from a prompt and reference texts.
     *
     * @param prompt The question prompt
     * @param referenceTexts List of reference texts to use
     * @return JSON string from Gemini
     */
    public String generateQuestions(String prompt, List<String> referenceTexts) throws IOException {
        // For demonstration: concatenate reference texts
        String combinedText = String.join("\n", referenceTexts);
        // Normally here you would call the Gemini API, return JSON string
        // Simulate a response (Java 11 compatible)
        return "{\n" +
                "  \"candidates\": [\n" +
                "    {\n" +
                "      \"content\": {\n" +
                "        \"parts\": [\n" +
                "          {\"text\": \"1. What is 2+2?\\nA) 3\\nB) 4\\nC) 5\\nD) 6\\nAnswer: B\"}\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    /**
     * Reads the contents of a PDF file into a string.
     */
    public String readPdf(File pdfFile) throws IOException {
        return new String(Files.readAllBytes(pdfFile.toPath()));
    }

    /**
     * Parses the Gemini JSON response into a list of Questions.
     */
    public List<Question> parseResponse(String json) throws IOException {
        List<Question> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        JsonNode candidatesNode = root.path("candidates");
        if (!candidatesNode.isArray()) return result;

        for (JsonNode candidate : candidatesNode) {
            JsonNode textNode = candidate.path("content").path("parts").get(0).path("text");
            if (textNode.isMissingNode()) continue;

            String[] lines = textNode.asText().split("\n");
            if (lines.length < 6) continue;

            String qText = lines[0].replaceFirst("^\\d+\\.\\s*", "").trim();
            List<String> options = new ArrayList<>();
            for (int i = 1; i <= 4; i++) options.add(lines[i].substring(3).trim());

            String answerLetter = lines[5].replace("Answer:", "").trim().toUpperCase();
            int correctIndex = -1;
            if ("A".equals(answerLetter)) correctIndex = 0;
            else if ("B".equals(answerLetter)) correctIndex = 1;
            else if ("C".equals(answerLetter)) correctIndex = 2;
            else if ("D".equals(answerLetter)) correctIndex = 3;

            result.add(new Question("Q" + (result.size() + 1), qText, options, correctIndex, ""));
        }

        return result;
    }
}