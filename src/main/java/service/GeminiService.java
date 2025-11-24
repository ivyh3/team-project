package service;

/**
 * Minimal service-level interface for Gemini interactions.
 * Keep it small so use-cases/controllers depend on this abstraction.
 */
public interface GeminiService {
    /**
     * Send a prompt to Gemini and return the raw response text.
     * Implementations may throw checked exceptions for API/IO errors.
     */
    String generateText(String prompt) throws Exception;
}
