package frameworks_drivers.gemini;

import okhttp3.*;
import java.io.IOException;

/**
 * Data access object for making JSON requests to Gemini API.
 * Clean architecture friendly: depends on abstraction, single responsibility.
 */
public class GeminiJsonApiDataAccessObject implements GeminiJsonApiDataAccess {

    private final OkHttpClient client;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // Dependency injection allows test mocks and config flexibility
    public GeminiJsonApiDataAccessObject(OkHttpClient client) {
        this.client = client;
    }

    public GeminiJsonApiDataAccessObject() {
        this(new OkHttpClient());
    }

    /**
     * Posts JSON data to the given URL and returns the response as a string.
     * @param url The endpoint URL
     * @param json The JSON payload
     * @return The response body as string
     * @throws IOException if the request fails
     */
    @Override
    public String postJson(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException("API Error: " + response.code() + " " + response.message());
            }

            if (response.body() == null) {
                throw new IOException("Empty response body");
            }

            return response.body().string();
        }
    }
}
