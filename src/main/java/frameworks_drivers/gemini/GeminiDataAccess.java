package frameworks_drivers.gemini;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Question;

import java.util.ArrayList;
import java.util.List;

public class GeminiDataAccess extends GeminiDataAccessObject {

    @Override
    public List<Question> generateQuiz(String prompt, List<String> referenceMaterials) {
        // Prepare request text
        String referenceText = String.join("\n\n", referenceMaterials);

        // Simulate Gemini API call (replace with actual API code)
        String rawJson = callGeminiApi(prompt, referenceText);

        try {
            return parseGeminiResponse(rawJson);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String callGeminiApi(String prompt, String referenceText) {
        // TODO: Implement actual Gemini API call
        // For now, return dummy JSON
        return "{\n"
            + "  \"candidates\": [\n"
            + "    {\n"
            + "      \"content\": {\n"
            + "        \"parts\": [\n"
            + "          { \"text\": \"1. What is 2+2?\\\\nA) 3\\\\nB) 4\\\\nC) 5\\\\nD) 6\\\\nAnswer: B\" }\n"
            + "        ]\n"
            + "      }\n"
            + "    }\n"
            + "  ]\n"
            + "}\n";
    }

    private List<Question> parseGeminiResponse(String response) throws Exception {
        List<Question> questions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);

        JsonNode candidatesNode = root.path("candidates");
        if (candidatesNode.isMissingNode() || !candidatesNode.isArray()) return questions;

        for (JsonNode candidate : candidatesNode) {
            JsonNode textNode = candidate.path("content").path("parts").get(0).path("text");
            if (textNode.isMissingNode()) continue;

            String[] blocks = textNode.asText().split("\\n\\n");
            for (String block : blocks) {
                if (!block.contains("A)") || !block.contains("Answer:")) continue;

                String[] lines = block.split("\\n");
                if (lines.length < 6) continue;

                String qText = lines[0].replaceFirst("^\\d+\\.\\s*", "").trim();
                List<String> choices = new ArrayList<>();
                for (int i = 1; i <= 4; i++) choices.add(lines[i].substring(3).trim());

                String letter = lines[5].replace("Answer:", "").trim().substring(0, 1).toUpperCase();
                int correctIndex;
                switch (letter) {
                    case "A": correctIndex = 0; break;
                    case "B": correctIndex = 1; break;
                    case "C": correctIndex = 2; break;
                    case "D": correctIndex = 3; break;
                    default: correctIndex = -1;
                }

                questions.add(new Question("Q" + (questions.size() + 1), qText, choices, correctIndex, ""));
            }
        }

        return questions;
    }
}
