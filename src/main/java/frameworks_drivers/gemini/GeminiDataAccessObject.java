package frameworks_drivers.gemini;

import app.Config;
import com.google.gson.*;
import entity.Question;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Implements both GeminiQuizDataAccess and GeminiFileDataAccess.
 * Handles AI-powered quiz generation and file management via Google's Gemini API.
 */
public class GeminiDataAccessObject implements GeminiQuizDataAccess, GeminiFileDataAccess {

    private final String apiKey;
    private final String apiUrl;
    private final Gson gson;
    private final OkHttpClient client;

    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 1000;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public GeminiDataAccessObject() {
        this.apiKey = Config.getGeminiApiKey();
        this.apiUrl = Config.getGeminiApiUrl();
        this.gson = new Gson();
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public List<Question> generateQuiz(String prompt, List<String> referenceMaterialTexts) throws GeminiApiException {
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

    public String uploadFileToGemini(byte[] fileContent, String mimeType, String displayName) throws GeminiApiException {
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

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new GeminiApiException("File upload failed: " + response.code() + " " + response.message());
                }

                String responseBody = response.body().string();
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                String fileUri = jsonResponse.getAsJsonObject("file").get("uri").getAsString();

                scheduleFileDeletion(fileUri);

                return fileUri;
            }

        } catch (IOException e) {
            throw new GeminiApiException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    public void deleteFileFromGemini(String fileUri) throws GeminiApiException {
        try {
            String fileName = fileUri.substring(fileUri.lastIndexOf('/') + 1);
            String deleteUrl = "https://generativelanguage.googleapis.com/v1beta/files/" + fileName + "?key=" + apiKey;

            Request request = new Request.Builder().url(deleteUrl).delete().build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful() && response.code() != 204) {
                    throw new GeminiApiException("File deletion failed: " + response.code() + " " + response.message());
                }
            }

        } catch (IOException e) {
            throw new GeminiApiException("Failed to delete file: " + e.getMessage(), e);
        }
    }

    public String generateText(String prompt) throws GeminiApiException {
        return callGeminiApiWithRetry(prompt, null);
    }

    // ===========================
    // Helper Methods
    // ===========================

    private String buildContext(List<String> referenceMaterialTexts) {
        if (referenceMaterialTexts == null || referenceMaterialTexts.isEmpty()) return "No reference materials.";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < referenceMaterialTexts.size(); i++) {
            sb.append("=== Reference Material ").append(i+1).append(" ===\n");
            sb.append(referenceMaterialTexts.get(i)).append("\n\n");
        }
        return sb.toString();
    }

    private String buildPrompt(String userPrompt, String contextText) {
        return "You are an AI tutor. Study Material:\n" + contextText + "\nUser Focus: " + userPrompt +
                "\nGenerate 5 MCQs in JSON format only.";
    }

    private String buildPromptForFiles(String userPrompt) {
        return "You are an AI tutor. Based on uploaded files, generate 5 MCQs in JSON format only.\nUser Focus: " + userPrompt;
    }

    private String callGeminiApiWithRetry(String prompt, List<String> fileUris) throws GeminiApiException {
        IOException lastIo = null;
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                return callGeminiApi(prompt, fileUris);
            } catch (IOException e) {
                lastIo = e;
                if (attempt < MAX_RETRIES - 1) {
                    try { Thread.sleep(RETRY_DELAY_MS * (attempt + 1)); }
                    catch (InterruptedException ie) { Thread.currentThread().interrupt(); throw new GeminiApiException("Interrupted", ie);}
                }
            }
        }
        throw new GeminiApiException("Failed after retries", lastIo);
    }

    private String callGeminiApi(String prompt, List<String> fileUris) throws IOException, GeminiApiException {
        JsonObject requestBody = buildRequestBody(prompt, fileUris);
        RequestBody body = RequestBody.create(gson.toJson(requestBody), JSON);

        Request request = new Request.Builder().url(apiUrl + "?key=" + apiKey).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new GeminiApiException("API error " + response.code());
            if (response.body() == null) throw new GeminiApiException("Response body null");
            return extractTextFromResponse(response.body().string());
        }
    }

    private JsonObject buildRequestBody(String prompt, List<String> fileUris) {
        JsonObject root = new JsonObject();
        root.add("contents", createContentJsonArray(prompt, fileUris));

        JsonObject config = new JsonObject();
        config.addProperty("temperature", 0.7);
        config.addProperty("topK", 40);
        config.addProperty("topP", 0.95);
        config.addProperty("maxOutputTokens", 1024);
        root.add("generationConfig", config);
        return root;
    }

    private JsonArray createContentJsonArray(String prompt, List<String> fileUris) {
        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();

        if (fileUris != null) {
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

    private String extractTextFromResponse(String responseBody) throws GeminiApiException {
        try {
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
            JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
            JsonArray parts = firstCandidate.getAsJsonObject("content").getAsJsonArray("parts");
            return parts.get(0).getAsJsonObject().get("text").getAsString();
        } catch (Exception e) {
            throw new GeminiApiException("Failed to parse response", e);
        }
    }

    private List<Question> parseQuestions(String responseText) throws GeminiApiException {
        try {
            String jsonText = responseText.trim();
            if (jsonText.startsWith("```json")) jsonText = jsonText.substring(7);
            else if (jsonText.startsWith("```")) jsonText = jsonText.substring(3);
            if (jsonText.endsWith("```")) jsonText = jsonText.substring(0, jsonText.length()-3);

            JsonArray arr = JsonParser.parseString(jsonText).getAsJsonArray();
            List<Question> questions = new ArrayList<>();
            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                String qText = obj.get("question").getAsString();
                JsonArray optionsArr = obj.getAsJsonArray("options");
                int correctIndex = obj.get("correctIndex").getAsInt();
                List<String> options = new ArrayList<>();
                for (JsonElement opt : optionsArr) options.add(opt.getAsString());
                questions.add(new Question(UUID.randomUUID().toString(), qText, options, correctIndex, ""));
            }
            return questions;
        } catch (Exception e) {
            throw new GeminiApiException("Failed to parse questions", e);
        }
    }

    private void scheduleFileDeletion(String fileUri) {
        Thread deletionThread = new Thread(() -> {
            try {
                Thread.sleep(3600 * 1000L); // 1 hour delay
                deleteFileFromGemini(fileUri);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (GeminiApiException e) {
                System.err.println("Failed to delete file: " + e.getMessage());
            }
        });
        deletionThread.setDaemon(true);
        deletionThread.start();
    }

    public static class GeminiApiException extends Exception {
        public GeminiApiException(String msg) { super(msg); }
        public GeminiApiException(String msg, Throwable cause) { super(msg, cause); }
    }
}
