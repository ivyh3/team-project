package frameworks_drivers.gemini;

import java.io.IOException;

/**
 * Interface for Gemini JSON API access.
 * Use cases depend on this abstraction instead of concrete HTTP clients.
 */
public interface GeminiJsonApiDataAccess {
    /**
     * Sends a JSON payload to the given URL and returns the response body as a string.
     *
     * @param url  the endpoint URL
     * @param json the JSON payload
     * @return the response body
     * @throws IOException if the request fails
     */
    String postJson(String url, String json) throws IOException;
}
