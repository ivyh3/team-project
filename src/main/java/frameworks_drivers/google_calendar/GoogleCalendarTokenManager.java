package frameworks_drivers.google_calendar;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Manages Google Calendar OAuth tokens in Firestore.
 * Stores and retrieves access/refresh tokens for users.
 */
public class GoogleCalendarTokenManager {
	private static final String COLLECTION_NAME = "google_calendar_tokens";
	private final Firestore firestore;

	public GoogleCalendarTokenManager() {
		this.firestore = FirestoreClient.getFirestore();
	}

	/**
	 * Saves Google Calendar tokens for a user.
	 * 
	 * @param userId       the user ID
	 * @param accessToken  the access token
	 * @param refreshToken the refresh token
	 * @param expiresIn    token expiration time in seconds
	 */
	public void saveTokens(String userId, String accessToken, String refreshToken, int expiresIn) {
		Map<String, Object> tokenData = new HashMap<>();
		tokenData.put("accessToken", accessToken);
		tokenData.put("refreshToken", refreshToken);
		tokenData.put("expiresIn", expiresIn);
		tokenData.put("linkedAt", com.google.cloud.Timestamp.now());

		try {
			firestore.collection(COLLECTION_NAME)
					.document(userId)
					.set(tokenData)
					.get();
			System.out.println("Google Calendar tokens saved for user: " + userId);
		} catch (InterruptedException | ExecutionException e) {
			System.err.println("Error saving Google Calendar tokens: " + e.getMessage());
		}
	}

	/**
	 * Retrieves Google Calendar tokens for a user.
	 * 
	 * @param userId the user ID
	 * @return CalendarTokens object or null if not found
	 */
	public CalendarTokens getTokens(String userId) {
		try {
			var docSnapshot = firestore.collection(COLLECTION_NAME)
					.document(userId)
					.get()
					.get();

			if (!docSnapshot.exists()) {
				return null;
			}

			Map<String, Object> data = docSnapshot.getData();
			return new CalendarTokens(
					(String) data.get("accessToken"),
					(String) data.get("refreshToken"),
					((Long) data.get("expiresIn")).intValue());
		} catch (InterruptedException | ExecutionException e) {
			System.err.println("Error getting Google Calendar tokens: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Updates the access token for a user (after refresh).
	 * 
	 * @param userId      the user ID
	 * @param accessToken the new access token
	 * @param expiresIn   token expiration time in seconds
	 */
	public void updateAccessToken(String userId, String accessToken, int expiresIn) {
		Map<String, Object> updates = new HashMap<>();
		updates.put("accessToken", accessToken);
		updates.put("expiresIn", expiresIn);

		try {
			firestore.collection(COLLECTION_NAME)
					.document(userId)
					.update(updates)
					.get();
			System.out.println("Access token updated for user: " + userId);
		} catch (InterruptedException | ExecutionException e) {
			System.err.println("Error updating access token: " + e.getMessage());
		}
	}

	/**
	 * Checks if a user has linked their Google Calendar.
	 * 
	 * @param userId the user ID
	 * @return true if tokens exist
	 */
	public boolean isCalendarLinked(String userId) {
		try {
			var docSnapshot = firestore.collection(COLLECTION_NAME)
					.document(userId)
					.get()
					.get();
			return docSnapshot.exists();
		} catch (InterruptedException | ExecutionException e) {
			System.err.println("Error checking calendar link status: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Removes Google Calendar tokens for a user (unlink).
	 * 
	 * @param userId the user ID
	 */
	public void unlinkCalendar(String userId) {
		try {
			firestore.collection(COLLECTION_NAME)
					.document(userId)
					.delete()
					.get();
			System.out.println("Google Calendar unlinked for user: " + userId);
		} catch (InterruptedException | ExecutionException e) {
			System.err.println("Error unlinking calendar: " + e.getMessage());
		}
	}

	/**
	 * Inner class to hold calendar tokens.
	 */
	public static class CalendarTokens {
		private final String accessToken;
		private final String refreshToken;
		private final int expiresIn;

		public CalendarTokens(String accessToken, String refreshToken, int expiresIn) {
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
