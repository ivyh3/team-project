package frameworks_drivers.gemini;

import app.Config;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entity.Question;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Headers;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Service for Gemini API operations.
 * Handles AI-powered quiz generation using Google's Gemini API.
 */
public class GeminiService implements GeminiDataAccess {
    private final String apiKey;
    private final String apiUrl;
    private final Gson gson;
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 1000;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;

    public GeminiService() {
        this.apiKey = Config.getGeminiApiKey();
        this.apiUrl = Config.getGeminiApiUrl();
        this.gson = new Gson();
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public List<Question> generateQuiz(String prompt, List<String> referenceMaterialTexts)
            throws GeminiApiException {
        String contextText = buildContext(referenceMaterialTexts);
        String fullPrompt = buildPrompt(prompt, contextText);
        String response = callGeminiApiWithRetry(fullPrompt, null);
        return parseQuestions(response);
    }

    public List<Question> generateQuizWithFiles(String prompt, List<String> fileUris) throws GeminiApiException {
        String fullPrompt = buildPromptForFiles(prompt);
        String response = callGeminiApiWithRetry(fullPrompt, fileUris);
        return parseQuestions(response);
    }

    private String callGeminiApiWithRetry(String prompt, List<String> fileUris) throws GeminiApiException {
        IOException lastIo = null;
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                return callGeminiApi(prompt, fileUris);
            } catch (IOException e) {
                lastIo = e;
                if (attempt < MAX_RETRIES - 1) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS * (attempt + 1));
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new GeminiApiException("Request interrupted", ie);
                    }
                }
            }
        }
        throw new GeminiApiException("Failed after " + MAX_RETRIES + " attempts", lastIo);
    }

    private String callGeminiApi(String prompt, List<String> fileUris) throws IOException, GeminiApiException {
        JsonObject requestBody = buildRequestBody(prompt, fileUris);
        String jsonRequest = gson.toJson(requestBody);

        RequestBody body = RequestBody.create(jsonRequest, JSON);
        Request request = new Request.Builder()
                .url(apiUrl + "?key=" + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new GeminiApiException("API returned error " + response.code() + ": " + response.message());
            }

            assert response.body() != null;
            String responseBody = response.body().string();
            return extractTextFromResponse(responseBody);
        }
    }

    private JsonObject buildRequestBody(String prompt, List<String> fileUris) {
        JsonObject root = new JsonObject();
        JsonArray contents = createContentJsonArray(prompt, fileUris);
        root.add("contents", contents);

        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("temperature", 0.7);
        generationConfig.addProperty("topK", 40);
        generationConfig.addProperty("topP", 0.95);
        generationConfig.addProperty("maxOutputTokens", 1024);
        root.add("generationConfig", generationConfig);

        return root;
    }

    private static JsonArray createContentJsonArray(String prompt, List<String> fileUris) {
        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();

        if (fileUris != null && !fileUris.isEmpty()) {
            for (String uri : fileUris) {
                JsonObject filePart = new JsonObject();
                JsonObject fileData = new JsonObject();
                fileData.addProperty("file_uri", uri);
                filePart.add("file_data", fileData);
                parts.add(filePart);
            }
        }

        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", prompt);
        parts.add(textPart);

        content.add("parts", parts);
        contents.add(content);
        return contents;
    }

    @NotNull
    private static JsonArray getJsonElements(String prompt, List<String> fileUris) {
        return createContentJsonArray(prompt, fileUris);
    }

    private String extractTextFromResponse(String responseBody) throws GeminiApiException {
        try {
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

            if (!jsonResponse.has("candidates")) {
                throw new GeminiApiException("Response missing 'candidates' field");
            }

            JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
            if (candidates.isEmpty()) {
                throw new GeminiApiException("No candidates in response");
            }

            JsonObject candidate = candidates.get(0).getAsJsonObject();
            JsonObject content = candidate.getAsJsonObject("content");
            JsonArray parts = content.getAsJsonArray("parts");

            if (parts.isEmpty()) {
                throw new GeminiApiException("No parts in response");
            }

            return parts.get(0).getAsJsonObject().get("text").getAsString();

        } catch (Exception e) {
            throw new GeminiApiException("Failed to parse response: " + e.getMessage(), e);
        }
    }

    private List<Question> parseQuestions(String responseText) throws GeminiApiException {
        try {
            String jsonText = extractJsonFromText(responseText);

            JsonArray questionsArray = JsonParser.parseString(jsonText).getAsJsonArray();
            List<Question> questions = new ArrayList<>();

            for (int i = 0; i < questionsArray.size(); i++) {
                JsonObject questionObj = questionsArray.get(i).getAsJsonObject();

                String questionText = questionObj.get("question").getAsString();
                JsonArray optionsArray = questionObj.getAsJsonArray("options");
                int correctIndex = questionObj.get("correctIndex").getAsInt();
                String explanation = questionObj.has("explanation") ? questionObj.get("explanation").getAsString() : "";

                List<String> options = new ArrayList<>();
                for (int j = 0; j < optionsArray.size(); j++) {
                    options.add(optionsArray.get(j).getAsString());
                }

                // Validate index and convert to the correct answer string expected by Question constructor
                if (correctIndex < 0 || correctIndex >= options.size()) {
                    throw new GeminiApiException("Invalid correctIndex " + correctIndex + " for question: " + questionText);
                }
                String correctAnswer = options.get(correctIndex);

                Question question = new Question(UUID.randomUUID().toString(), questionText, options, correctAnswer, explanation);
                questions.add(question);
            }

            return questions;

        } catch (Exception e) {
            throw new GeminiApiException("Failed to parse questions: " + e.getMessage() + "\nResponse: " + responseText, e);
        }
    }

    private String extractJsonFromText(String text) {
        text = text.trim();
        if (text.startsWith("```json")) {
            text = text.substring(7);
        } else if (text.startsWith("```")) {
            text = text.substring(3);
        }
        if (text.endsWith("```")) {
            text = text.substring(0, text.length() - 3);
        }
        return text.trim();
    }

    private String buildContext(List<String> referenceMaterialTexts) {
        if (referenceMaterialTexts == null || referenceMaterialTexts.isEmpty()) {
            return "No reference materials provided.";
        }

        StringBuilder context = new StringBuilder();
        for (int i = 0; i < referenceMaterialTexts.size(); i++) {
            context.append("=== Reference Material ").append(i + 1).append(" ===\n");
            context.append(referenceMaterialTexts.get(i));
            context.append("\n\n");
        }
        return context.toString();
    }

    private String buildPrompt(String userPrompt, String contextText) {
        return String.format(
                """
                        You are an AI tutor helping students study. Based on the study material provided below, \
                        generate a quiz with 5 multiple choice questions.

                        Study Material:
                        %s

                        User's Focus: %s

                        IMPORTANT: Return ONLY a valid JSON array with this exact format:
                        [
                          {
                            "question": "Question text here?",
                            "options": ["Option A", "Option B", "Option C", "Option D"],
                            "correctIndex": 0,
                            "explanation": "Explanation of why this is correct"
                          }
                        ]

                        Rules:
                        - Generate exactly 5 questions
                        - Each question must have 4 options
                        - correctIndex is 0-based (0, 1, 2, or 3)
                        - Provide clear explanations
                        - Focus on the user's specified topic
                        - Return ONLY the JSON array, no other text""",
                contextText, userPrompt);
    }

    private String buildPromptForFiles(String userPrompt) {
        return String.format(
                """
                        You are an AI tutor helping students study. Based on the uploaded files, \
                        generate a quiz with 5 multiple choice questions.

                        User's Focus: %s

                        IMPORTANT: Return ONLY a valid JSON array with this exact format:
                        [
                          {
                            "question": "Question text here?",
                            "options": ["Option A", "Option B", "Option C", "Option D"],
                            "correctIndex": 0,
                            "explanation": "Explanation of why this is correct"
                          }
                        ]

                        Rules:
                        - Generate exactly 5 questions
                        - Each question must have 4 options
                        - correctIndex is 0-based (0, 1, 2, or 3)
                        - Provide clear explanations
                        - Focus on the user's specified topic
                        - Return ONLY the JSON array, no other text""",
                userPrompt);
    }

    public String uploadFileToGemini(byte[] fileContent, String mimeType, String displayName)
            throws GeminiApiException {
        try {
            String uploadUrl = "https://generativelanguage.googleapis.com/upload/v1beta/files?key=" + apiKey;

            JsonObject metadata = new JsonObject();
            JsonObject file = new JsonObject();
            file.addProperty("display_name", displayName);
            metadata.add("file", file);

            String boundary = "----Boundary" + System.currentTimeMillis();
            MultipartBody multipartBody = new MultipartBody.Builder(boundary)
                    .setType(MultipartBody.FORM)
                    .addPart(Headers.of("Content-Type", "application/json; charset=UTF-8"),
                            RequestBody.create(gson.toJson(metadata), MediaType.parse("application/json")))
                    .addPart(Headers.of("Content-Type", mimeType),
                            RequestBody.create(fileContent, MediaType.parse(mimeType)))
                    .build();

            Request request = new Request.Builder()
                    .url(uploadUrl)
                    .header("X-Goog-Upload-Protocol", "multipart")
                    .post(multipartBody)
                    .build();

            OkHttpClient uploadClient = client.newBuilder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build();

            try (Response response = uploadClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new GeminiApiException("File upload failed: " + response.code() + " " + response.message());
                }

                assert response.body() != null;
                String responseBody = response.body().string();
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonObject fileObj = jsonResponse.getAsJsonObject("file");
                String fileUri = fileObj.get("uri").getAsString();

                scheduleFileDeletion(fileUri);

                return fileUri;
            }

        } catch (IOException e) {
            throw new GeminiApiException("Failed to upload file to Gemini: " + e.getMessage(), e);
        }
    }

    private void scheduleFileDeletion(String fileUri) {
        Thread deletionThread = new Thread(() -> {
            try {
                Thread.sleep(3600 * 1000L);
                deleteFileFromGemini(fileUri);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (GeminiApiException e) {
                System.err.println("Failed to delete Gemini file " + fileUri + ": " + e.getMessage());
            }
        });
        deletionThread.setDaemon(true);
        deletionThread.start();
    }

    public void deleteFileFromGemini(String fileUri) throws GeminiApiException {
        try {
            String fileName = fileUri.substring(fileUri.lastIndexOf('/') + 1);
            String deleteUrl = "https://generativelanguage.googleapis.com/v1beta/files/" + fileName + "?key="
                    + apiKey;

            Request request = new Request.Builder()
                    .url(deleteUrl)
                    .delete()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful() && response.code() != 204) {
                    throw new GeminiApiException(
                            "File deletion failed: " + response.code() + " " + response.message());
                }
            }

        } catch (IOException e) {
            throw new GeminiApiException("Failed to delete file from Gemini: " + e.getMessage(), e);
        }
    }

    public static class GeminiApiException extends Exception {
        public GeminiApiException(String message) {
            super(message);
        }

        public GeminiApiException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @Override
    public String generateText(String prompt) throws Exception {
        return callGeminiApiWithRetry(prompt, null);
    }
}
