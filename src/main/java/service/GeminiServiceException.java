package service;

/**
 * Checked exception for errors in the Gemini service layer.
 */
public class GeminiServiceException extends Exception {
    public GeminiServiceException(String message) {
        super(message);
    }

    public GeminiServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
