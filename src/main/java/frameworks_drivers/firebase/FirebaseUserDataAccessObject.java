package frameworks_drivers.firebase;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import app.Config;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonObject;
import entity.User;
import entity.UserFactory;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

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
    private static final String FIREBASE_AUTH_URL = "https://identitytoolkit.googleapis.com/v1/accounts";
    private final Firestore firestore;
    private final UserFactory userFactory;
    private final FirebaseAuth firebaseAuth;
    private final OkHttpClient client;
    private final String firebaseWebApiKey;

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
    public User getUserByEmail(String email) {
        User result;
        try {
            final UserRecord userRecord = this.firebaseAuth.getUserByEmail(email);

            // createdTimestamp is in milliseconds
            final long createdTimestamp = userRecord.getUserMetadata().getCreationTimestamp();
            final LocalDateTime createdAt = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(createdTimestamp),
                    ZoneId.systemDefault());

            result = userFactory.create(userRecord.getUid(), email, createdAt);
        }
        catch (FirebaseAuthException event) {
            System.err.println("Error getting user: " + event.getMessage());
            result = null;
        }
        return result;
    }

    /**
     * Gets the email for a given user ID.
     *
     * @param userId the user ID
     * @return the user's email
     */
    @Override
    public User getUserByUserId(String userId) {
        User result;
        try {
            final UserRecord userRecord = firebaseAuth.getUser(userId);

            // createdTimestamp is in milliseconds
            final long createdTimestamp = userRecord.getUserMetadata().getCreationTimestamp();
            final LocalDateTime createdAt = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(createdTimestamp),
                    ZoneId.systemDefault());

            result = userFactory.create(userId, userRecord.getEmail(), createdAt);
        }
        catch (FirebaseAuthException event) {
            System.err.println("Error getting user email: " + event.getMessage());
            result = null;
        }
        return result;
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
        final CreateRequest createUserRequest = new CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(password)
                .setDisabled(false);

        try {
            final UserRecord userRecord = firebaseAuth.createUser(createUserRequest);
            final String userId = userRecord.getUid();

            createUserDocument(userId);

            System.out.println("Successfully created new user: " + userId);
        }
        catch (FirebaseAuthException event) {
            System.err.println("Error creating user: " + event.getMessage());
        }
        catch (ExecutionException event) {
            System.err.println("Execution error creating Firestore document: " + event.getMessage());
        }
        catch (InterruptedException event) {
            System.err.println("Interruption error creating Firestore document: " + event.getMessage());
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
        boolean result;
        // Build request body
        final JsonObject requestBody = new JsonObject();
        requestBody.addProperty("email", email);
        requestBody.addProperty("password", password);
        requestBody.addProperty("returnSecureToken", true);

        final String url = FIREBASE_AUTH_URL + ":signInWithPassword?key=" + firebaseWebApiKey;

        final RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json"));

        final Request request = new Request.Builder().url(url).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            result = response.isSuccessful();
        }
        catch (IOException event) {
            System.err.println("Error signing in: " + event.getMessage());
            result = false;
        }
        return result;
    }

    /**
     * Checks if a user with the given email exists.
     *
     * @param email the email to look for
     * @return true if a user with the given email exists; false otherwise
     */
    @Override
    public boolean existsByEmail(String email) {
        boolean result;
        try {
            firebaseAuth.getUserByEmail(email);
            result = true;
        }
        catch (FirebaseAuthException event) {
            result = false;
        }
        return result;
    }

    /**
     * Updates the user's password using Firebase Admin SDK.
     *
     * @param userId      the user ID whose password is to be updated
     * @param newPassword the new password
     */
    @Override
    public void changePassword(String userId, String newPassword) {
        try {
            final UpdateRequest request = new UpdateRequest(userId)
                    .setPassword(newPassword);

            firebaseAuth.updateUser(request);
            System.out.println("Successfully updated password for user: " + userId);
        }
        catch (FirebaseAuthException event) {
            System.err.println("Error updating password: " + event.getMessage());
        }
    }

    /**
     * Creates an empty document in Firestore for the given user ID.
     *
     * @param userId the user ID to create a folder for
     * @throws InterruptedException if the request fails
     * @throws ExecutionException   if the request fails
     */

    private void createUserDocument(String userId) throws InterruptedException, ExecutionException {
        // Create empty Firestore document for user
        final Map<String, Object> userData = new HashMap<>();
        // blocking call to ensure write completes
        firestore.collection(USERS_COLLECTION)
                .document(userId)
                .set(userData)
                .get();
    }
}
