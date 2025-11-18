package app;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Configuration class for managing API keys and application settings.
 * Uses Dotenv to load environment variables from .env file.
 */
public class Config {
	private static Dotenv dotenv;
	private static boolean firebaseInitialized = false;

	static {
		try {
			dotenv = Dotenv.configure()
					.directory(".")
					.load();
		} catch (Exception e) {
			System.err.println("Warning: Could not load .env file: " + e.getMessage());
		}
	}

	// ============================================
	// Firebase Initialization
	// ============================================

	/**
	 * Initializes Firebase Admin SDK with credentials from src/main/resources
	 * This should be called once at application startup.
	 *
	 * @throws IOException if the credentials file cannot be read
	 */
	public static void initializeFirebase() throws IOException {
		if (firebaseInitialized) { // Prevent multiple Firebase initializations
			return;
		}

		String credentialsPath = getFirebaseCredentialsPath();
		FileInputStream serviceAccount = new FileInputStream(credentialsPath);

		FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setStorageBucket(getFirebaseStorageBucket())
				.build();

		FirebaseApp.initializeApp(options);
		firebaseInitialized = true;

		System.out.println("Firebase initialized successfully");
	}

	/**
	 * Returns the initialized FirebaseApp instance.
	 *
	 * @return the FirebaseApp instance
	 * @throws IllegalStateException if Firebase has not been initialized
	 */
	public static FirebaseApp getFirebaseApp() {
		if (!firebaseInitialized) {
			throw new IllegalStateException("Firebase has not been initialized. Call initializeFirebase() first.");
		}
		return FirebaseApp.getInstance();
	}

	/**
	 * Checks if Firebase has been initialized.
	 *
	 * @return true if Firebase is initialized, false otherwise
	 */
	public static boolean isFirebaseInitialized() {
		return firebaseInitialized;
	}

	// ============================================
	// Dotenv Variables Getters
	// ============================================

	public static String getFirebaseCredentialsPath() {
		return dotenv.get("FIREBASE_CREDENTIALS_PATH", "");
	}

	public static String getFirebaseStorageBucket() {
		return dotenv.get("FIREBASE_STORAGE_BUCKET", "");
	}

	public static String getFirebaseWebApiKey() {
		return dotenv.get("FIREBASE_WEB_API_KEY", "");
	}

	public static String getGeminiApiKey() {
		return dotenv.get("GEMINI_API_KEY", "");
	}

	public static String getGeminiApiUrl() {
		return dotenv.get("GEMINI_API_URL",
				"https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent");
	}

	public static String getGoogleOAuthClientId() {
		return dotenv.get("GOOGLE_OAUTH_CLIENT_ID", "");
	}

	public static String getGoogleOAuthClientSecret() {
		return dotenv.get("GOOGLE_OAUTH_CLIENT_SECRET", "");
	}

	public static String getGoogleOAuthRedirectUri() {
		return dotenv.get("GOOGLE_OAUTH_REDIRECT_URI",
				"http://localhost:8080/oauth/callback");
	}
}
