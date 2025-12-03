package app;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public final class Config {
    private static Config instance;
    private static Dotenv dotenv;
    private static boolean firebaseInitialized;

    private Config() {
        loadDotenv();
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    static void loadDotenv() {
        try {
            dotenv = Dotenv.configure().directory(".").load();
            System.out.println("Environment variables loaded successfully");
        } catch (DotenvException e) {
            System.err.println("Warning: Could not load .env file: " + e.getMessage());
        }
    }

    public static synchronized void initializeFirebase() throws IOException {
        if (!firebaseInitialized) {
            String credentialsPath = getFirebaseCredentialsPath();
            if (credentialsPath.isEmpty()) {
                throw new IllegalStateException("FIREBASE_CREDENTIALS_PATH is not set in environment variables.");
            }

            FileInputStream serviceAccount = new FileInputStream(credentialsPath);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(getFirebaseStorageBucket())
                    .build();

            FirebaseApp.initializeApp(options);
            firebaseInitialized = true;

            System.out.println("Firebase initialized successfully");
        }
    }

    public static FirebaseApp getFirebaseApp() {
        if (!firebaseInitialized) {
            throw new IllegalStateException("Firebase has not been initialized. Call initializeFirebase() first.");
        }
        return FirebaseApp.getInstance();
    }

    public static boolean isFirebaseInitialized() {
        return firebaseInitialized;
    }

    // ===== Dotenv getters =====
    public static String getFirebaseCredentialsPath() {
        if (dotenv == null) loadDotenv();
        return dotenv.get("FIREBASE_CREDENTIALS_PATH", "");
    }

    public static String getFirebaseStorageBucket() {
        if (dotenv == null) loadDotenv();
        return dotenv.get("FIREBASE_STORAGE_BUCKET", "");
    }

    public static String getFirebaseWebApiKey() {
        if (dotenv == null) loadDotenv();
        return dotenv.get("FIREBASE_WEB_API_KEY", "");
    }

    public static String getGeminiApiKey() {
        if (dotenv == null) loadDotenv();
        return dotenv.get("GEMINI_API_KEY", "");
    }

    public static String getGeminiApiUrl() {
        if (dotenv == null) loadDotenv();
        return dotenv.get("GEMINI_API_URL",
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent");
    }

    public static String getGoogleOAuthClientId() {
        if (dotenv == null) loadDotenv();
        return dotenv.get("GOOGLE_OAUTH_CLIENT_ID", "");
    }

    public static String getGoogleOAuthClientSecret() {
        if (dotenv == null) loadDotenv();
        return dotenv.get("GOOGLE_OAUTH_CLIENT_SECRET", "");
    }

    public static String getGoogleOAuthRedirectUri() {
        if (dotenv == null) loadDotenv();
        return dotenv.get("GOOGLE_OAUTH_REDIRECT_URI", "http://localhost:8080/oauth/callback");
    }
}