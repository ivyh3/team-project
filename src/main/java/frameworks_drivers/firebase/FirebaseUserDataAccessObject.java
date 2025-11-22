package frameworks_drivers.firebase;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.Timestamp;
import okhttp3.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import app.Config;
import entity.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Firebase Data Access Object for User entities.
 * Handles both Firebase Authentication and Firestore data persistence.
 * Combines authentication operations with CRUD operations for User entities.
 */
public class FirebaseUserDataAccessObject {
    private static final String USERS_COLLECTION = "users";
    private final Firestore firestore;
    private final FirebaseAuth firebaseAuth;
    private final OkHttpClient client;
    private final String firebaseWebApiKey;
    private static final String FIREBASE_AUTH_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword";
    private static final String FIREBASE_TOKEN_URL = "https://securetoken.googleapis.com/v1/token";

    public FirebaseUserDataAccessObject() {
        this.firestore = FirestoreClient.getFirestore();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.client = new OkHttpClient();
        this.firebaseWebApiKey = Config.getFirebaseWebApiKey();
    }

    public User getById(String userId) {
        try {
            var docSnapshot = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .get()
                    .get();

            if (!docSnapshot.exists()) {
                return null;
            }

            Map<String, Object> data = docSnapshot.getData();

            // Convert Firestore Timestamp to LocalDateTime
            Timestamp timestamp = (Timestamp) data.get("createdAt");
            LocalDateTime createdAt = LocalDateTime.ofInstant(
                    timestamp.toDate().toInstant(),
                    ZoneId.systemDefault());

            return new User(
                    docSnapshot.getId(),
                    (String) data.get("name"),
                    (String) data.get("email"),
                    createdAt);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error getting user: " + e.getMessage());
            return null;
        }
    }

    public void save(User user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("createdAt", com.google.cloud.Timestamp.now());

        try {
            firestore.collection(USERS_COLLECTION)
                    .document(user.getId())
                    .set(userData)
                    .get();
            System.out.println("User saved successfully: " + user.getId());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }

    public void update(User user) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", user.getName());
        updates.put("email", user.getEmail());

        try {
            firestore.collection(USERS_COLLECTION)
                    .document(user.getId())
                    .update(updates)
                    .get();
            System.out.println("User updated successfully: " + user.getId());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
    }

    public void delete(String userId) {
        try {
            firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .delete()
                    .get();
            System.out.println("User deleted successfully: " + userId);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }

    public boolean existsByEmail(String email) {
        try {
            var querySnapshot = firestore.collection(USERS_COLLECTION)
                    .whereEqualTo("email", email)
                    .get()
                    .get();
            return !querySnapshot.isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
            return false;
        }
    }

    // ========================================
    // Firebase Authentication Operations
    // ========================================

    /**
     * Creates a user with email and password.
     * 
     * @param email    the user's email
     * @param password the user's password
     * @return the user ID
     */
    public String createUserWithEmail(String email, String password) {
        CreateRequest createUserRequest = new CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(password)
                .setDisabled(false);

        try {
            UserRecord userRecord = firebaseAuth.createUser(createUserRequest);
            System.out.println("Successfully created new user: " + userRecord.getUid());
            return userRecord.getUid();
        } catch (FirebaseAuthException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return null;
        }
    }

    /**
     * Signs in a user with email and password.
     * Returns an AuthResult containing the user ID and token.
     * 
     * @param email    the user's email
     * @param password the user's password
     * @return AuthResult with userId and idToken, or null if sign-in fails
     */
    public AuthResult signInWithEmail(String email, String password) {
        // Build request body
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("email", email);
        requestBody.addProperty("password", password);
        requestBody.addProperty("returnSecureToken", true);

        String url = FIREBASE_AUTH_URL + "?key=" + firebaseWebApiKey;

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json"));

        Request request = new Request.Builder().url(url).post(body).build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.err.println("Sign-in failed: " + response.code() + " " + response.message());
                return null;
            }

            String responseBody = response.body().string();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

            String idToken = jsonResponse.get("idToken").getAsString();
            String userId = jsonResponse.get("localId").getAsString();
            String refreshToken = jsonResponse.get("refreshToken").getAsString();
            long expiresIn = jsonResponse.get("expiresIn").getAsLong();

            System.out.println("Successfully signed in user: " + userId);
            return new AuthResult(userId, idToken, refreshToken, expiresIn);
        } catch (IOException e) {
            System.err.println("Error signing in: " + e.getMessage());
            return null;
        }
    }

    /**
     * Refreshes an expired ID token using the refresh token.
     * 
     * @param refreshToken the refresh token
     * @return new AuthResult with fresh tokens, or null if refresh fails
     */
    public AuthResult refreshIdToken(String refreshToken) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("grant_type", "refresh_token");
        requestBody.addProperty("refresh_token", refreshToken);

        String url = FIREBASE_TOKEN_URL + "?key=" + firebaseWebApiKey;

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Token refresh failed: " + response.code() + " " + response.message());
                return null;
            }

            String responseBody = response.body().string();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

            String newIdToken = jsonResponse.get("id_token").getAsString();
            String newRefreshToken = jsonResponse.get("refresh_token").getAsString();
            String userId = jsonResponse.get("user_id").getAsString();
            long expiresIn = jsonResponse.get("expires_in").getAsLong();

            System.out.println("Successfully refreshed token for user: " + userId);
            return new AuthResult(userId, newIdToken, newRefreshToken, expiresIn);
        } catch (IOException e) {
            System.err.println("Error refreshing token: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets user information by user ID using Firebase Admin SDK.
     * 
     * @param userId the user ID
     * @return UserRecord with user details, or null if not found
     */
    public UserRecord getUserById(String userId) {
        try {
            return firebaseAuth.getUser(userId);
        } catch (FirebaseAuthException e) {
            System.err.println("Error getting user: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets user information by email using Firebase Admin SDK.
     * 
     * @param email the user's email
     * @return UserRecord with user details, or null if not found
     */
    public UserRecord getUserByEmail(String email) {
        try {
            return firebaseAuth.getUserByEmail(email);
        } catch (FirebaseAuthException e) {
            System.err.println("Error getting user by email: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes a user from Firebase Authentication.
     * 
     * @param userId the user ID to delete
     * @return true if deletion succeeded, false otherwise
     */
    public boolean deleteUserAuth(String userId) {
        try {
            firebaseAuth.deleteUser(userId);
            System.out.println("Successfully deleted user from auth: " + userId);
            return true;
        } catch (FirebaseAuthException e) {
            System.err.println("Error deleting user from auth: " + e.getMessage());
            return false;
        }
    }

    /**
     * Signs out the current user.
     */
    public void signOut() {
    }

    /**
     * Inner class to hold authentication result.
     */
    public static class AuthResult {
        private final String userId;
        private final String idToken;
        private final String refreshToken;
        private final long expiresIn;

        public AuthResult(String userId, String idToken, String refreshToken, long expiresIn) {
            this.userId = userId;
            this.idToken = idToken;
            this.refreshToken = refreshToken;
            this.expiresIn = expiresIn;
        }

        public String getUserId() {
            return userId;
        }

        public String getIdToken() {
            return idToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public long getExpiresIn() {
            return expiresIn;
        }
    }
}
