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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Service for Gemini API operations.
 * Handles AI-powered quiz generation using Google's Gemini API.
 */
public class GeminiService {
	private final OkHttpClient client;
	private final String apiKey;
	private final String apiUrl;
	private final Gson gson;
	private static final int MAX_RETRIES = 3;
	private static final int RETRY_DELAY_MS = 1000;

	public GeminiService() {
		this.apiKey = Config.getGeminiApiKey();
		this.apiUrl = Config.getGeminiApiUrl();
		this.gson = new Gson();
		this.client = new OkHttpClient.Builder()
				.connectTimeout(30, TimeUnit.SECONDS)
				.readTimeout(60, TimeUnit.SECONDS)
				.build();
	}

	/**
	 * Generates a quiz based on the prompt and reference materials.
	 * 
	 * @param prompt                 the user's prompt describing what to study
	 * @param referenceMaterialTexts the text content of reference materials
	 * @return list of generated questions
	 * @throws GeminiApiException if the API call fails
	 */
	public List<Question> generateQuiz(String prompt, List<String> referenceMaterialTexts)
			throws GeminiApiException {
		// Build context from reference materials
		String contextText = buildContext(referenceMaterialTexts);

		// Build the complete prompt
		String fullPrompt = buildPrompt(prompt, contextText);

		// Make API call with retries
		String response = callGeminiApiWithRetry(fullPrompt, null);

		// Parse and return questions
		return parseQuestions(response);
	}

	/**
	 * Generates a quiz using uploaded file URIs from Gemini File API.
	 * 
	 * @param prompt   the user's prompt describing what to study
	 * @param fileUris list of file URIs from uploadFileToGemini()
	 * @return list of generated questions
	 * @throws GeminiApiException if the API call fails
	 */
	public List<Question> generateQuizWithFiles(String prompt, List<String> fileUris) throws GeminiApiException {
		// Build enhanced prompt for files
		String fullPrompt = buildPromptForFiles(prompt);

		// Make API call with file references
		String response = callGeminiApiWithRetry(fullPrompt, fileUris);

		// Parse and return questions
		return parseQuestions(response);
	}

	/**
	 * Makes a call to the Gemini API with retry logic.
	 * 
	 * @param prompt   the prompt to send
	 * @param fileUris optional list of file URIs to include
	 * @return the API response text
	 * @throws GeminiApiException if all retries fail
	 */
	private String callGeminiApiWithRetry(String prompt, List<String> fileUris) throws GeminiApiException {
		Exception lastException = null;

		for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
			try {
				return callGeminiApi(prompt, fileUris);
			} catch (IOException e) {
				lastException = e;
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

		throw new GeminiApiException("Failed after " + MAX_RETRIES + " attempts", lastException);
	}

	/**
	 * Makes a single call to the Gemini API.
	 * 
	 * @param prompt   the prompt to send
	 * @param fileUris optional list of file URIs to include
	 * @return the API response text
	 * @throws IOException        if the request fails
	 * @throws GeminiApiException if the API returns an error
	 */
	private String callGeminiApi(String prompt, List<String> fileUris) throws IOException, GeminiApiException {
		// Build request body
		JsonObject requestBody = buildRequestBody(prompt, fileUris);
		String jsonRequest = gson.toJson(requestBody);

		// Build request
		RequestBody body = RequestBody.create(jsonRequest, MediaType.parse("application/json"));
		Request request = new Request.Builder()
				.url(apiUrl + "?key=" + apiKey)
				.post(body)
				.build();

		// Execute request
		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new GeminiApiException("API returned error " + response.code() + ": " + response.message());
			}

			String responseBody = response.body().string();
			return extractTextFromResponse(responseBody);
		}
	}

	/**
	 * Builds the JSON request body for Gemini API.
	 * 
	 * @param prompt   the prompt text
	 * @param fileUris optional list of file URIs to include
	 * @return JSON object representing the request
	 */
	private JsonObject buildRequestBody(String prompt, List<String> fileUris) {
		JsonObject requestBody = new JsonObject();

		// Add contents array
		JsonArray contents = new JsonArray();
		JsonObject content = new JsonObject();
		JsonArray parts = new JsonArray();

		// Add file references if provided
		if (fileUris != null && !fileUris.isEmpty()) {
			for (String fileUri : fileUris) {
				JsonObject filePart = new JsonObject();
				JsonObject fileData = new JsonObject();
				fileData.addProperty("file_uri", fileUri);
				filePart.add("file_data", fileData);
				parts.add(filePart);
			}
		}

		// Add text prompt
		JsonObject part = new JsonObject();
		part.addProperty("text", prompt);
		parts.add(part);

		content.add("parts", parts);
		contents.add(content);
		requestBody.add("contents", contents);

		// Add generation config for JSON output
		JsonObject generationConfig = new JsonObject();
		generationConfig.addProperty("temperature", 0.7);
		generationConfig.addProperty("topK", 40);
		generationConfig.addProperty("topP", 0.95);
		generationConfig.addProperty("maxOutputTokens", 2048);
		requestBody.add("generationConfig", generationConfig);

		return requestBody;
	}

	/**
	 * Extracts the text content from Gemini API response.
	 * 
	 * @param responseBody the raw API response
	 * @return the text content
	 * @throws GeminiApiException if parsing fails
	 */
	private String extractTextFromResponse(String responseBody) throws GeminiApiException {
		try {
			JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

			if (!jsonResponse.has("candidates")) {
				throw new GeminiApiException("Response missing 'candidates' field");
			}

			JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
			if (candidates.size() == 0) {
				throw new GeminiApiException("No candidates in response");
			}

			JsonObject candidate = candidates.get(0).getAsJsonObject();
			JsonObject content = candidate.getAsJsonObject("content");
			JsonArray parts = content.getAsJsonArray("parts");

			if (parts.size() == 0) {
				throw new GeminiApiException("No parts in response");
			}

			return parts.get(0).getAsJsonObject().get("text").getAsString();

		} catch (Exception e) {
			throw new GeminiApiException("Failed to parse response: " + e.getMessage(), e);
		}
	}

	/**
	 * Parses questions from the API response text.
	 * 
	 * @param responseText the text containing JSON array of questions
	 * @return list of Question entities
	 * @throws GeminiApiException if parsing fails
	 */
	private List<Question> parseQuestions(String responseText) throws GeminiApiException {
		try {
			// Extract JSON array from response (might be wrapped in markdown)
			String jsonText = extractJsonFromText(responseText);

			JsonArray questionsArray = JsonParser.parseString(jsonText).getAsJsonArray();
			List<Question> questions = new ArrayList<>();

			for (int i = 0; i < questionsArray.size(); i++) {
				JsonObject questionObj = questionsArray.get(i).getAsJsonObject();

				String questionText = questionObj.get("question").getAsString();
				JsonArray optionsArray = questionObj.getAsJsonArray("options");
				int correctIndex = questionObj.get("correctIndex").getAsInt();
				String explanation = questionObj.has("explanation") ? questionObj.get("explanation").getAsString()
						: "";

				// Convert options to List<String>
				List<String> options = new ArrayList<>();
				for (int j = 0; j < optionsArray.size(); j++) {
					options.add(optionsArray.get(j).getAsString());
				}

				// Create Question entity
				Question question = new Question(UUID.randomUUID().toString(), questionText, options, correctIndex,
						explanation);

				questions.add(question);
			}

			return questions;

		} catch (Exception e) {
			throw new GeminiApiException(
					"Failed to parse questions: " + e.getMessage() + "\nResponse: " + responseText, e);
		}
	}

	/**
	 * Extracts JSON array from text that might contain markdown formatting.
	 * 
	 * @param text the text containing JSON
	 * @return the JSON string
	 */
	private String extractJsonFromText(String text) {
		// Remove markdown code blocks if present
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

	/**
	 * Builds context text from reference materials.
	 * 
	 * @param referenceMaterialTexts list of text from reference materials
	 * @return combined context text
	 */
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

	/**
	 * Builds a prompt for quiz generation with text context.
	 * 
	 * @param userPrompt  the user's prompt
	 * @param contextText the context from reference materials
	 * @return the formatted prompt
	 */
	private String buildPrompt(String userPrompt, String contextText) {
		return String.format(
				"You are an AI tutor helping students study. Based on the study material provided below, "
						+ "generate a quiz with 5 multiple choice questions.\n\n" + "Study Material:\n%s\n\n"
						+ "User's Focus: %s\n\n"
						+ "IMPORTANT: Return ONLY a valid JSON array with this exact format:\n" + "[\n" + "  {\n"
						+ "    \"question\": \"Question text here?\",\n"
						+ "    \"options\": [\"Option A\", \"Option B\", \"Option C\", \"Option D\"],\n"
						+ "    \"correctIndex\": 0,\n"
						+ "    \"explanation\": \"Explanation of why this is correct\"\n" + "  }\n" + "]\n\n"
						+ "Rules:\n" + "- Generate exactly 5 questions\n" + "- Each question must have 4 options\n"
						+ "- correctIndex is 0-based (0, 1, 2, or 3)\n" + "- Provide clear explanations\n"
						+ "- Focus on the user's specified topic\n"
						+ "- Return ONLY the JSON array, no other text",
				contextText, userPrompt);
	}

	/**
	 * Builds a prompt for quiz generation with file references.
	 * 
	 * @param userPrompt the user's prompt
	 * @return the formatted prompt
	 */
	private String buildPromptForFiles(String userPrompt) {
		return String.format(
				"You are an AI tutor helping students study. Based on the uploaded files, "
						+ "generate a quiz with 5 multiple choice questions.\n\n" + "User's Focus: %s\n\n"
						+ "IMPORTANT: Return ONLY a valid JSON array with this exact format:\n" + "[\n" + "  {\n"
						+ "    \"question\": \"Question text here?\",\n"
						+ "    \"options\": [\"Option A\", \"Option B\", \"Option C\", \"Option D\"],\n"
						+ "    \"correctIndex\": 0,\n"
						+ "    \"explanation\": \"Explanation of why this is correct\"\n" + "  }\n" + "]\n\n"
						+ "Rules:\n" + "- Generate exactly 5 questions\n" + "- Each question must have 4 options\n"
						+ "- correctIndex is 0-based (0, 1, 2, or 3)\n" + "- Provide clear explanations\n"
						+ "- Focus on the user's specified topic\n"
						+ "- Return ONLY the JSON array, no other text",
				userPrompt);
	}

	/**
	 * Uploads a file to Gemini's file API with 1-hour expiry.
	 * 
	 * @param fileContent the file content
	 * @param mimeType    the MIME type (e.g., "application/pdf", "text/plain")
	 * @param displayName the display name for the file
	 * @return the file URI to use in Gemini requests
	 * @throws GeminiApiException if upload fails
	 */
	public String uploadFileToGemini(byte[] fileContent, String mimeType, String displayName)
			throws GeminiApiException {
		try {
			String uploadUrl = "https://generativelanguage.googleapis.com/upload/v1beta/files?key=" + apiKey;

			// Build metadata JSON
			JsonObject metadata = new JsonObject();
			JsonObject file = new JsonObject();
			file.addProperty("display_name", displayName);
			metadata.add("file", file);

			// Build multipart body
			String boundary = "----Boundary" + System.currentTimeMillis();
			MultipartBody multipartBody = new MultipartBody.Builder(boundary)
					.setType(MultipartBody.FORM)
					.addPart(Headers.of("Content-Type", "application/json; charset=UTF-8"),
							RequestBody.create(gson.toJson(metadata), MediaType.parse("application/json")))
					.addPart(Headers.of("Content-Type", mimeType),
							RequestBody.create(fileContent, MediaType.parse(mimeType)))
					.build();

			// Build request
			Request request = new Request.Builder()
					.url(uploadUrl)
					.header("X-Goog-Upload-Protocol", "multipart")
					.post(multipartBody)
					.build();

			// Execute with longer timeout for file upload
			OkHttpClient uploadClient = client.newBuilder()
					.readTimeout(120, TimeUnit.SECONDS)
					.build();

			try (Response response = uploadClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					throw new GeminiApiException("File upload failed: " + response.code() + " " + response.message());
				}

				String responseBody = response.body().string();
				JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
				JsonObject fileObj = jsonResponse.getAsJsonObject("file");
				String fileUri = fileObj.get("uri").getAsString();

				// Schedule deletion after 1 hour
				scheduleFileDeletion(fileUri, 3600);

				return fileUri;
			}

		} catch (IOException e) {
			throw new GeminiApiException("Failed to upload file to Gemini: " + e.getMessage(), e);
		}
	}

	/**
	 * Schedules a file for deletion after a delay.
	 * 
	 * @param fileUri      the file URI to delete
	 * @param delaySeconds the delay in seconds before deletion
	 */
	private void scheduleFileDeletion(String fileUri, int delaySeconds) {
		Thread deletionThread = new Thread(() -> {
			try {
				Thread.sleep(delaySeconds * 1000L);
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

	/**
	 * Deletes a file from Gemini's file API.
	 * 
	 * @param fileUri the file URI (e.g., "files/abc123")
	 * @throws GeminiApiException if deletion fails
	 */
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

	/**
	 * Custom exception for Gemini API errors.
	 */
	public static class GeminiApiException extends Exception {
		public GeminiApiException(String message) {
			super(message);
		}

		public GeminiApiException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
