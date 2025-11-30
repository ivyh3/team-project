package frameworks_drivers.firebase;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import entity.UserFactory;
import okhttp3.*;
import com.google.gson.JsonObject;
import app.Config;
import entity.User;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.IOException;
import java.time.Instant;
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
public class FirebaseUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface {
    private static final String USERS_COLLECTION = "users";
    private final Firestore firestore;
    private final UserFactory userFactory;
    private final FirebaseAuth firebaseAuth;
    private final OkHttpClient client;
    private final String firebaseWebApiKey;
    private static final String FIREBASE_AUTH_URL = "https://identitytoolkit.googleapis.com/v1/accounts";

    public FirebaseUserDataAccessObject(UserFactory userFactory) {
        this.userFactory = userFactory;
        this.firestore = FirestoreClient.getFirestore();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.client = new OkHttpClient();
        this.firebaseWebApiKey = Config.getFirebaseWebApiKey();
    }

    /**
     * Returns the user with the given email.
     *
     * @param email the email to look up
     * @return the user with the given email
     */
    @Override
    public User getUser(String email) {
        try {
            UserRecord userRecord = this.firebaseAuth.getUserByEmail(email);

            long createdTimestamp = userRecord.getUserMetadata().getCreationTimestamp(); // in milliseconds
            LocalDateTime createdAt = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(createdTimestamp),
                    ZoneId.systemDefault());

            return userFactory.create(userRecord.getUid(), email, createdAt);
        } catch (FirebaseAuthException e) {
            System.err.println("Error getting user: " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates a new user with the given email and password.
     * Creates a document in Firestore named after the new userId.
     *
     * @param email    the email of the new user
     * @param password the password of the new user
     */
    @Override
    public void createUser(String email, String password) {
        CreateRequest createUserRequest = new CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(password)
                .setDisabled(false);

        try {
            UserRecord userRecord = firebaseAuth.createUser(createUserRequest);
            String userId = userRecord.getUid();

            createUserDocument(userId);

            System.out.println("Successfully created new user: " + userId);
        } catch (FirebaseAuthException e) {
            System.err.println("Error creating user: " + e.getMessage());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error creating Firestore document: " + e.getMessage());
        }
    }

    /**
     * Verifies if the given password matches the password of a user with the given
     * email.
     *
     * @param email    the email to look for
     * @param password the given password to verify
     * @return true if given password matches the password of a user with the given
     *         email; false otherwise
     */
    @Override
    public boolean verifyPassword(String email, String password) {
        // Build request body
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("email", email);
        requestBody.addProperty("password", password);
        requestBody.addProperty("returnSecureToken", true);

        String url = FIREBASE_AUTH_URL + ":signInWithPassword?key=" + firebaseWebApiKey;

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json"));

        Request request = new Request.Builder().url(url).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            System.err.println("Error signing in: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a user with the given email exists.
     *
     * @param email the email to look for
     * @return true if a user with the given email exists; false otherwise
     */
    @Override
    public boolean existsByEmail(String email) {
        try {
            firebaseAuth.getUserByEmail(email);
            return true;
        } catch (FirebaseAuthException e) {
            return false;
        }
    }

    /**
     * Updates the system to record this user's password.
     *
     * @param user the user whose password is to be updated
     */
    @Override
    public void changePassword(User user) {

    }

    /**
     * Returns the username of the current user of the application.
     *
     * @return the username of the current user
     */
    @Override
    public String getCurrentUsername() {
        return "";
    }

    /**
     * Sets the username indicating who is the current user of the application.
     *
     * @param username the new current username
     */
    @Override
    public void setCurrentUsername(String username) {

    }

    /**
     * Creates a empty document in Firestore for the given user ID.
     *
     * @param userId the user ID to create a folder for
     */

    private void createUserDocument(String userId) throws InterruptedException, ExecutionException {
        // Create empty Firestore document for user
        Map<String, Object> userData = new HashMap<>();
        firestore.collection(USERS_COLLECTION)
                .document(userId)
                .set(userData)
                .get(); // blocking call to ensure write completes
    }
}
