package frameworks_drivers.google_calendar;

import app.Config;
import okhttp3.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Service for OAuth 2.0 operations.
 * Handles OAuth flow for Google Calendar API access.
 */
public class OAuthService {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final OkHttpClient client;
    private static final String OAUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String REVOKE_URL = "https://oauth2.googleapis.com/revoke";
    private static final String CALENDAR_SCOPE = "https://www.googleapis.com/auth/calendar";

    public OAuthService() {
        this.clientId = Config.getGoogleOAuthClientId();
        this.clientSecret = Config.getGoogleOAuthClientSecret();
        this.redirectUri = Config.getGoogleOAuthRedirectUri();
        this.client = new OkHttpClient();
    }

    /**
     * Generates OAuth authorization URL for Google Calendar access.
     * 
     * @param state the state parameter for CSRF protection (typically userId)
     * @return the authorization URL
     */
    public String getAuthorizationUrl(String state) {
        try {
            String scope = URLEncoder.encode(CALENDAR_SCOPE, StandardCharsets.UTF_8.toString());
            String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.toString());
            String encodedState = URLEncoder.encode(state, StandardCharsets.UTF_8.toString());

            return OAUTH_URL + "?client_id=" + clientId
                    + "&redirect_uri=" + encodedRedirectUri
                    + "&response_type=code"
                    + "&scope=" + scope
                    + "&access_type=offline"
                    + "&prompt=consent"
                    + "&state=" + encodedState;
        } catch (Exception e) {
            System.err.println("Error building authorization URL: " + e.getMessage());
            return null;
        }
    }

    /**
     * Exchanges authorization code for tokens.
     * 
     * @param code the authorization code from OAuth callback
     * @return TokenResponse with access_token, refresh_token, and expires_in
     */
    public TokenResponse exchangeCodeForTokens(String code) {
        FormBody requestBody = new FormBody.Builder()
                .add("code", code)
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("redirect_uri", redirectUri)
                .add("grant_type", "authorization_code")
                .build();

        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Token exchange failed: " + response.code() + " " + response.message());
                return null;
            }

            String responseBody = response.body().string();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

            String accessToken = jsonResponse.get("access_token").getAsString();
            String refreshToken = jsonResponse.has("refresh_token")
                    ? jsonResponse.get("refresh_token").getAsString()
                    : null;
            int expiresIn = jsonResponse.get("expires_in").getAsInt();

            System.out.println("Successfully exchanged code for tokens");
            return new TokenResponse(accessToken, refreshToken, expiresIn);
        } catch (IOException e) {
            System.err.println("Error exchanging code for tokens: " + e.getMessage());
            return null;
        }
    }

    /**
     * Refreshes an access token using a refresh token.
     * 
     * @param refreshToken the refresh token
     * @return new TokenResponse with fresh access_token
     */
    public TokenResponse refreshAccessToken(String refreshToken) {
        FormBody requestBody = new FormBody.Builder()
                .add("refresh_token", refreshToken)
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("grant_type", "refresh_token")
                .build();

        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Token refresh failed: " + response.code() + " " + response.message());
                return null;
            }

            String responseBody = response.body().string();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

            String accessToken = jsonResponse.get("access_token").getAsString();
            int expiresIn = jsonResponse.get("expires_in").getAsInt();

            System.out.println("Successfully refreshed access token");
            // Refresh token is not returned again, so pass null
            return new TokenResponse(accessToken, null, expiresIn);
        } catch (IOException e) {
            System.err.println("Error refreshing token: " + e.getMessage());
            return null;
        }
    }

    /**
     * Revokes a token (access or refresh token).
     * 
     * @param token the token to revoke
     * @return true if revocation succeeded
     */
    public boolean revokeToken(String token) {
        FormBody requestBody = new FormBody.Builder()
                .add("token", token)
                .build();

        Request request = new Request.Builder()
                .url(REVOKE_URL)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Token revocation failed: " + response.code() + " " + response.message());
                return false;
            }

            System.out.println("Successfully revoked token");
            return true;
        } catch (IOException e) {
            System.err.println("Error revoking token: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inner class to represent token response.
     */
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
        private int expiresIn;

        public TokenResponse(String accessToken, String refreshToken, int expiresIn) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.expiresIn = expiresIn;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public int getExpiresIn() {
            return expiresIn;
        }
    }
}
