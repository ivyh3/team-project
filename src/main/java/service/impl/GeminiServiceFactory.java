package service.impl;

import service.GeminiService;

/**
 * Factory for creating GeminiService instances.
 * Keeps controllers/use-cases independent of concrete implementations.
 */
public final class GeminiServiceFactory {

    private GeminiServiceFactory() {
        // Prevent instantiation
    }

    public static GeminiService create(String apiKey, String apiUrl) {
        return new GeminiServiceHttp(apiKey, apiUrl);
    }
}
