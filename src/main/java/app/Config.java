package app;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

/**
 * Configuration class for managing API keys and application settings.
 * Uses Dotenv to load environment variables from .env file.
 */
public class Config {
    private static final Dotenv dotenv;
    private static boolean firebaseInitialized = false;

    static {
        Dotenv tmp;
        try {
            tmp = Dotenv.configure()
                    .ignoreIfMissing() // don't fail if .env isn't present
                    .load();
        } catch (Exception e) {
            tmp = null;
        }
        dotenv = tmp;
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
        String credsPath = getFirebaseCredentialsPath();

        File credsFile = new File(credsPath);
        if (!credsFile.exists()) {
            throw new IOException("Firebase credentials file not found at: " + credsPath
                    + ". Provide the file or set FIREBASE_CREDENTIALS_PATH env var.");
        }

        try (FileInputStream serviceAccount = new FileInputStream(credsFile)) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
            firebaseInitialized = true;
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

    public static String getFirebaseCredentialsPath() {
        return getEnv("FIREBASE_CREDENTIALS_PATH", "firebase.json");
    }

    public static String getFirebaseStorageBucket() {
        return getEnv("FIREBASE_STORAGE_BUCKET", "");
    }

    public static String getFirebaseWebApiKey() {
        return getEnv("FIREBASE_WEB_API_KEY", "");
    }
    public static String getGeminiApiKey() {
        return getEnv("GEMINI_API_KEY", "");
    }

    public static String getGeminiApiUrl() {
        return getEnv("GEMINI_API_URL",
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent");
    }

    public static String getGoogleOAuthClientId() {
        return getEnv("GOOGLE_OAUTH_CLIENT_ID", "");
    }

    public static String getGoogleOAuthClientSecret() {
        return getEnv("GOOGLE_OAUTH_CLIENT_SECRET", "");
    }

    public static String getGoogleOAuthRedirectUri() {
        return getEnv("GOOGLE_OAUTH_REDIRECT_URI",
                "http://localhost:8080/oauth/callback");
    }

    private static String getEnv(String key, String defaultValue) {
        String val = null;
        if (dotenv != null) {
            val = dotenv.get(key);
        }
        if (val == null || val.isEmpty()) {
            val = System.getenv(key);
        }
        return (val == null || val.isEmpty()) ? defaultValue : val;
    }
}
