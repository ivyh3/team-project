package app;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

/**
 * Configuration class for managing API keys and application settings.
 * Uses Dotenv to load environment variables from .env file.
 * Implements the Singleton pattern to ensure only one instance exists.
 */
public final class Config {
    private static Config instance;
    private static Dotenv dotenv;
    private static boolean firebaseInitialized;

    // Private constructor to prevent instantiation of utility class.
    private Config() {
    }

    /**
     * Gets the singleton instance of Config.
     * Automatically loads Dotenv on first access.
     *
     * @return the singleton Config instance
     */
    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
            loadDotenv();
        }
        return instance;
    }

    // ============================================
    // Dotenv Initialization
    // ============================================

    /**
     * Loads Dotenv from project .env file.
     * This should be called before any other Config methods.
     */
    public static void loadDotenv() {
        try {
            dotenv = Dotenv.configure()
                    .directory(".")
                    .load();
            System.out.println("Environment variables loaded successfully");
        } catch (DotenvException event) {
            System.err.println("Warning: Could not load .env file: " + event.getMessage());
            System.err.println("Proceeding with default/system environment variables");
        }
    }

    // ============================================
    // Firebase Initialization
    // ============================================

    /**
     * Initializes Firebase Admin SDK with credentials from src/main/resources.
     * This should be called once at application startup, after loadDotenv().
     *
     * @throws IOException if the credentials file cannot be read
     */
    public static void initializeFirebase() throws IOException {
        // Prevent multiple Firebase initializations
        if (!firebaseInitialized) {
            final String credentialsPath = getFirebaseCredentialsPath();
            final FileInputStream serviceAccount = new FileInputStream(credentialsPath);

            final FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(getFirebaseStorageBucket())
                    .build();

            FirebaseApp.initializeApp(options);
            firebaseInitialized = true;

            System.out.println("Firebase initialized successfully");
        } else {
            System.out.println("Firebase has been already initialized");
        }
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

    /**
     * Gets the Firebase credentials path from environment variables.
     *
     * @return the Firebase credentials path
     */
    public static String getFirebaseCredentialsPath() {
        if (dotenv == null) {
            loadDotenv();
        }
        return dotenv.get("FIREBASE_CREDENTIALS_PATH", "");
    }

    /**
     * Gets the Firebase storage bucket from environment variables.
     *
     * @return the Firebase storage bucket
     */
    public static String getFirebaseStorageBucket() {
        if (dotenv == null) {
            loadDotenv();
        }
        return dotenv.get("FIREBASE_STORAGE_BUCKET", "");
    }

    /**
     * Gets the Firebase Web API key from environment variables.
     *
     * @return the Firebase Web API key
     */
    public static String getFirebaseWebApiKey() {
        if (dotenv == null) {
            loadDotenv();
        }
        return dotenv.get("FIREBASE_WEB_API_KEY", "");
    }

    /**
     * Gets the Gemini API key from environment variables.
     *
     * @return the Gemini API key
     */
    public static String getGeminiApiKey() {
        if (dotenv == null) {
            loadDotenv();
        }
        return dotenv.get("GEMINI_API_KEY", "");
    }

    /**
     * Gets the Gemini API URL from environment variables.
     *
     * @return the Gemini API URL
     */
    public static String getGeminiApiUrl() {
        if (dotenv == null) {
            loadDotenv();
        }
        return dotenv.get("GEMINI_API_URL",
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent");
    }

    /**
     * Gets the Google OAuth client ID from environment variables.
     *
     * @return the Google OAuth client ID
     */
    public static String getGoogleOAuthClientId() {
        if (dotenv == null) {
            loadDotenv();
        }
        return dotenv.get("GOOGLE_OAUTH_CLIENT_ID", "");
    }

    /**
     * Gets the Google OAuth client secret from environment variables.
     *
     * @return the Google OAuth client secret
     */
    public static String getGoogleOAuthClientSecret() {
        if (dotenv == null) {
            loadDotenv();
        }
        return dotenv.get("GOOGLE_OAUTH_CLIENT_SECRET", "");
    }

    /**
     * Gets the Google OAuth redirect URI from environment variables.
     *
     * @return the Google OAuth redirect URI
     */
    public static String getGoogleOAuthRedirectUri() {
        if (dotenv == null) {
            loadDotenv();
        }
        return dotenv.get("GOOGLE_OAUTH_REDIRECT_URI",
                "http://localhost:8080/oauth/callback");
    }
}
