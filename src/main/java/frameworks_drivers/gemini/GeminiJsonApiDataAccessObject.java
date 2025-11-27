package frameworks_drivers.gemini;

import okhttp3.*;
import java.io.IOException;

public class GeminiJsonApiDataAccessObject {
    private final OkHttpClient client;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public GeminiJsonApiDataAccessObject() {
        this.client = new OkHttpClient();
    }

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