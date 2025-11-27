package service;

/**
 * Service-level interface for interacting with Gemini API.
 * Use-case and controllers depend on this abstraction.
 */
public interface GeminiService {
    /**
     * Sends a prompt to Gemini and returns the raw response text.
     * Implementations may throw a checked GeminiServiceException.
     *
     * @param prompt the text prompt
     * @return the response text from Gemini
     * @throws GeminiServiceException if API or IO errors occur
     */
    String generateText(String prompt) throws GeminiServiceException;
}
