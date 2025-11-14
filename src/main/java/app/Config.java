package app;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Configuration class for managing API keys and application settings.
 * Uses Dotenv to load environment variables from .env file.
 */
public class Config {
	private static Dotenv dotenv;

	static {
		try {
			dotenv = Dotenv.configure()
					.directory(".")
					.ignoreIfMissing()
					.load();
		} catch (Exception e) {
			System.err.println("Warning: Could not load .env file: " + e.getMessage());
			System.err.println("Falling back to system environment variables");
			dotenv = Dotenv.configure()
					.ignoreIfMissing()
					.ignoreIfMalformed()
					.systemProperties()
					.load();
		}
	}

	// ============================================
	// Firebase Configuration
	// ============================================

	public static String getFirebaseProjectId() {
		return dotenv.get("FIREBASE_PROJECT_ID", "");
	}

	public static String getFirebaseStorageBucket() {
		return dotenv.get("FIREBASE_STORAGE_BUCKET", "");
	}

	public static String getFirebaseCredentialsPath() {
		return dotenv.get("FIREBASE_CREDENTIALS_PATH", "src/main/resources/firebase-config.json");
	}

	// ============================================
	// Gemini API Configuration
	// ============================================

	public static String getGeminiApiKey() {
		return dotenv.get("GEMINI_API_KEY", "");
	}

	public static String getGeminiApiUrl() {
		return dotenv.get("GEMINI_API_URL",
				"https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent");
	}

	// ============================================
	// Google OAuth Configuration
	// ============================================

	public static String getGoogleOAuthClientId() {
		return dotenv.get("GOOGLE_OAUTH_CLIENT_ID", "");
	}

	public static String getGoogleOAuthClientSecret() {
		return dotenv.get("GOOGLE_OAUTH_CLIENT_SECRET", "");
	}

	public static String getGoogleOAuthRedirectUri() {
		return dotenv.get("GOOGLE_OAUTH_REDIRECT_URI", "http://localhost:8080/oauth/callback");
	}
}
