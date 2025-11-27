package service.impl;

import okhttp3.*;
import service.GeminiService;
import service.GeminiServiceException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * HTTP implementation of GeminiService using OkHttp.
 * Uses constructor injection for API key and URL (SOLID principle).
 */
public class GeminiServiceHttp implements GeminiService {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final String apiKey;
    private final String apiUrl;
    private final OkHttpClient client;

    public GeminiServiceHttp(String apiKey, String apiUrl) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public String generateText(String prompt) throws GeminiServiceException {
        RequestBody body = RequestBody.create(prompt, JSON);

        Request request = new Request.Builder()
                .url(apiUrl + "?key=" + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new GeminiServiceException(
                        "Gemini API error: " + response.code() + " " + response.message());
            }
            if (response.body() == null) {
                throw new GeminiServiceException("Gemini API returned empty body");
            }
            return response.body().string();
        } catch (IOException e) {
            throw new GeminiServiceException("Failed to call Gemini API", e);
        }
    }
}
