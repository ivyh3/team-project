package service.impl;

import service.GeminiService;

public class GeminiServiceFactory {
    /**
     * Create a GeminiService using environment variables:
     * - GEMINI_API_KEY (required)
     * - GEMINI_API_ENDPOINT (optional, default provided)
     */
    public static GeminiService createFromEnv() {
        String apiKey = System.getenv("GEMINI_API_KEY");
        String endpoint = System.getenv().getOrDefault("GEMINI_API_ENDPOINT", "https://api.gemini.google/v1/generate");
        return new GeminiServiceHttp(endpoint, apiKey);
    }
}
