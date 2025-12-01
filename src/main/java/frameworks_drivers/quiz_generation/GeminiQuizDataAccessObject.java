package frameworks_drivers.quiz_generation;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.Config;
import entity.Question;
import use_case.generate_quiz.GenerateQuizDataAccessInterface;

public class GeminiQuizDataAccessObject implements GenerateQuizDataAccessInterface {
    private final String API_KEY;
    private final String API_URL;

    public GeminiQuizDataAccessObject() {
        this.API_KEY = Config.getGeminiApiKey();
        this.API_URL = Config.getGeminiApiUrl();
    }

    @Override
    public List<Question> generateQuizBase64(String base64Pdf, String context, int numQuestions) {
        String prompt = new StringBuilder()
                .append("You are a quiz generation AI. Generate simple multiple choice study questions. Make the questions fairly short. Each question has four options. Only one is correct. Only use plaintext, do not use markdown, do not use latex. Generate questions based on what the user is studying.\n")
                .append("The user says they are currently studying: ").append(context).append("\n")
                .append("Number of Questions: ").append(numQuestions).append("\n")
                .toString();

        try {
            // Build request body
            Map<String, Object> payload = new HashMap<>();
            Map<String, Object> inlineData = Map.of(
                    "mime_type", "application/pdf",
                    "data", base64Pdf);
            Map<String, Object> pdfPart = Map.of("inline_data", inlineData);
            Map<String, Object> promptPart = Map.of(
                    "text", prompt);
            // FIX: Prompt must come first in the parts array!
            Map<String, Object> content = Map.of(
                    "parts", List.of(promptPart, pdfPart));

            // Updated JSON schema for quiz questions
            Map<String, Object> questionObjectSchema = Map.of(
                    "type", "object",
                    "properties", Map.of(
                            "question", Map.of(
                                    "type", "string",
                                    "description", "The question text"),
                            "options", Map.of(
                                    "type", "array",
                                    "items", Map.of("type", "string"),
                                    "description", "Possible options for the question"),
                            "correct_option_index", Map.of(
                                    "type", "integer",
                                    "description", "The index of the correct option"),
                            "explanation", Map.of(
                                    "type", "string",
                                    "description", "Short explanation for the correct answer")),
                    "required", List.of("question", "options", "correct_option_index", "explanation"));

            Map<String, Object> responseJsonSchema = Map.of(
                    "type", "object",
                    "properties", Map.of(
                            "questions", Map.of(
                                    "type", "array",
                                    "description", "the questions for the study quiz",
                                    "items", questionObjectSchema)));

            Map<String, Object> generationConfig = Map.of(
                    "responseMimeType", "application/json",
                    "responseJsonSchema", responseJsonSchema);
            payload.put("contents", List.of(content));
            payload.put("generationConfig", generationConfig);

            // Serialize payload to JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(payload);

            // Build the POST request
            String url = API_URL;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .header("x-goog-api-key", API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Send the request
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // System.out.println(response.statusCode());
            // System.out.println(response.body());

            // Parse and return questions using the new method
            return parseQuestionsFromGeminiResponse(response.body());

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Extracts the quiz questions from the Gemini API response JSON.
     */
    private List<Question> parseQuestionsFromGeminiResponse(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode textNode = candidates.get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text");
                if (!textNode.isMissingNode()) {
                    String quizJson = textNode.asText();
                    JsonNode quizRoot = mapper.readTree(quizJson);
                    JsonNode questionsNode = quizRoot.path("questions");
                    if (questionsNode.isArray()) {
                        List<Question> questions = new java.util.ArrayList<>();
                        for (JsonNode q : questionsNode) {
                            String questionText = q.path("question").asText();
                            List<String> options = new java.util.ArrayList<>();
                            for (JsonNode opt : q.path("options")) {
                                options.add(opt.asText());
                            }
                            int correctIndex = q.path("correct_option_index").asInt();
                            String explanation = q.path("explanation").asText();
                            questions.add(new Question(questionText, options, correctIndex, explanation));
                        }
                        return questions;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to parse questions from Gemini response: " + e.getMessage());
        }
        return List.of();
    }

    @Override
    public List<Question> generateQuiz(byte[] pdfBytes, String context, int numQuestions) {
        String fileUri = uploadPdfBytesAndGetFileUri(pdfBytes, "study_material.pdf");
        if (fileUri == null) {
            System.out.println("[ERROR] PDF upload failed, cannot generate quiz.");
            return List.of();
        }

        String prompt = new StringBuilder()
                .append("You are a quiz generation AI. Generate simple multiple choice study questions. Make the questions fairly short. Each question has four options. Only one is correct. Only use plaintext, do not use markdown, do not use latex. Generate questions based on what the user is studying.\n")
                .append("The user says they are currently studying: ").append(context).append("\n")
                .append("Number of Questions: ").append(numQuestions).append("\n")
                .toString();

        try {
            // Build JSON schema for quiz questions
            Map<String, Object> questionObjectSchema = Map.of(
                    "type", "object",
                    "properties", Map.of(
                            "question", Map.of(
                                    "type", "string",
                                    "description", "The question text"),
                            "options", Map.of(
                                    "type", "array",
                                    "items", Map.of("type", "string"),
                                    "description", "Possible options for the question"),
                            "correct_option_index", Map.of(
                                    "type", "integer",
                                    "description", "The index of the correct option"),
                            "explanation", Map.of(
                                    "type", "string",
                                    "description", "Short explanation for the correct answer")),
                    "required", List.of("question", "options", "correct_option_index", "explanation"));

            Map<String, Object> responseJsonSchema = Map.of(
                    "type", "object",
                    "properties", Map.of(
                            "questions", Map.of(
                                    "type", "array",
                                    "description", "the questions for the study quiz",
                                    "items", questionObjectSchema)));

            Map<String, Object> generationConfig = Map.of(
                    "responseMimeType", "application/json",
                    "responseJsonSchema", responseJsonSchema);

            // Build the content parts: prompt and file_data
            Map<String, Object> promptPart = Map.of("text", prompt);
            Map<String, Object> fileDataPart = Map.of(
                    "file_data", Map.of(
                            "mime_type", "application/pdf",
                            "file_uri", fileUri));
            // FIX: Prompt must come first in the parts array!
            Map<String, Object> content = Map.of(
                    "parts", List.of(promptPart, fileDataPart));

            Map<String, Object> payload = new HashMap<>();
            payload.put("contents", List.of(content));
            payload.put("generationConfig", generationConfig);

            // Serialize payload to JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(payload);

            // Build the POST request
            String url = API_URL;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofMinutes(5)) // Large files may need more time
                    .header("x-goog-api-key", API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Send the request
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // System.out.println(response.statusCode());
            // System.out.println(response.body());

            // Parse and return questions
            return parseQuestionsFromGeminiResponse(response.body());

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Uploads a PDF byte array to the Gemini API using the resumable upload
     * protocol and returns the file URI.
     * 
     * @param pdfBytes    The PDF file as a byte array.
     * @param displayName Display name for the file in Gemini.
     * @return The file URI as a String, or null if upload fails.
     */
    private String uploadPdfBytesAndGetFileUri(byte[] pdfBytes, String displayName) {
        try {
            String apiKey = API_KEY;
            String baseUrl = "https://generativelanguage.googleapis.com";
            long numBytes = pdfBytes.length;

            // 1. Start resumable upload session
            String metadataJson = String.format("{\"file\": {\"display_name\": \"%s\"}}", displayName);
            HttpRequest startReq = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/upload/v1beta/files?key=" + apiKey))
                    .header("X-Goog-Upload-Protocol", "resumable")
                    .header("X-Goog-Upload-Command", "start")
                    .header("X-Goog-Upload-Header-Content-Length", String.valueOf(numBytes))
                    .header("X-Goog-Upload-Header-Content-Type", "application/pdf")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(metadataJson))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<Void> startResp = client.send(startReq, HttpResponse.BodyHandlers.discarding());

            // Extract upload URL from response headers
            String uploadUrl = startResp.headers()
                    .firstValue("x-goog-upload-url")
                    .orElse(null);

            if (uploadUrl == null) {
                System.out.println("[ERROR] Failed to get upload URL from Gemini API.");
                return null;
            }

            // 2. Upload the actual PDF bytes
            HttpRequest uploadReq = HttpRequest.newBuilder()
                    .uri(URI.create(uploadUrl))
                    // .header("Content-Length", String.valueOf(numBytes))
                    .header("X-Goog-Upload-Offset", "0")
                    .header("X-Goog-Upload-Command", "upload, finalize")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(pdfBytes))
                    .build();

            HttpResponse<String> uploadResp = client.send(uploadReq, HttpResponse.BodyHandlers.ofString());

            // 3. Parse file_uri from response JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode fileInfo = mapper.readTree(uploadResp.body());
            JsonNode fileUriNode = fileInfo.path("file").path("uri");
            if (fileUriNode.isMissingNode()) {
                System.out.println("[ERROR] file.uri not found in upload response.");
                return null;
            }
            String fileUri = fileUriNode.asText();
            System.out.println("[INFO] Uploaded file. file_uri=" + fileUri);
            return fileUri;

        } catch (IOException | InterruptedException e) {
            System.out.println("[ERROR] Exception during PDF upload: " + e.getMessage());
            return null;
        }
    }
}