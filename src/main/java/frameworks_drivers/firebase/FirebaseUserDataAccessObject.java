package frameworks_drivers.firebase;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.cloud.FirestoreClient;
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
 * Handles Firebase Authentication and Firestore persistence.
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

    @Override
    public User getUser(String email) {
        try {
            UserRecord userRecord = this.firebaseAuth.getUserByEmail(email);

            long createdTimestamp = userRecord.getUserMetadata().getCreationTimestamp();
            LocalDateTime createdAt = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(createdTimestamp),
                    ZoneId.systemDefault());

            return userFactory.create(userRecord.getUid(), email, createdAt);
        } catch (FirebaseAuthException e) {
            System.err.println("Error getting user: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void createUser(String email, String password) {
        CreateRequest createUserRequest = new CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(password)
                .setDisabled(false);

        try {
            UserRecord userRecord = firebaseAuth.createUser(createUserRequest);

            Map<String, Object> userData = new HashMap<>();

            firestore.collection(USERS_COLLECTION)
                    .document(userRecord.getUid())
                    .set(userData)
                    .get(); // blocking call

            System.out.println("Successfully created new user: " + userRecord.getUid());
        } catch (FirebaseAuthException e) {
            System.err.println("Error creating user: " + e.getMessage());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error creating Firestore document: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyPassword(String email, String password) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("email", email);
        requestBody.addProperty("password", password);
        requestBody.addProperty("returnSecureToken", true);

        String url = FIREBASE_AUTH_URL + ":signInWithPassword?key=" + firebaseWebApiKey;

        RequestBody body = RequestBody.create(requestBody.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder().url(url).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            System.err.println("Error signing in: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            firebaseAuth.getUserByEmail(email);
            return true;
        } catch (FirebaseAuthException e) {
            return false;
        }
    }

    @Override
    public void changePassword(User user) {
        // Java 11 safe: implementation left empty or add Firebase password update logic
    }

    @Override
    public String getCurrentUsername() {
        return "";
    }

    @Override
    public void setCurrentUsername(String username) {
        // no-op
    }
}
