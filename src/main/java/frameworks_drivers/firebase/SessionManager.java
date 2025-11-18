package frameworks_drivers.firebase;

/**
 * Manages the current user session in memory.
 * Stores authentication tokens and user ID for the currently signed-in user.
 * Singleton pattern to ensure only one session exists at a time.
 */
public class SessionManager {
	private static SessionManager instance;
	private String currentUserId;
	private String currentIdToken;
	private String currentRefreshToken;
	private long tokenExpirationTime;

	private SessionManager() {
		// Private constructor for singleton
	}

	/**
	 * Gets the singleton instance of SessionManager.
	 * 
	 * @return the SessionManager instance
	 */
	public static synchronized SessionManager getInstance() {
		if (instance == null) {
			instance = new SessionManager();
		}
		return instance;
	}

	/**
	 * Sets the current user session.
	 * 
	 * @param userId       the user ID
	 * @param idToken      the Firebase ID token
	 * @param refreshToken the refresh token
	 * @param expiresIn    token expiration time in seconds
	 */
	public void setSession(String userId, String idToken, String refreshToken, long expiresIn) {
		this.currentUserId = userId;
		this.currentIdToken = idToken;
		this.currentRefreshToken = refreshToken;
		// Calculate expiration time in milliseconds from now
		this.tokenExpirationTime = System.currentTimeMillis() + (expiresIn * 1000);
	}

	/**
	 * Clears the current session.
	 */
	public void clearSession() {
		this.currentUserId = null;
		this.currentIdToken = null;
		this.currentRefreshToken = null;
		this.tokenExpirationTime = 0;
	}

	/**
	 * Checks if a user is currently signed in.
	 * 
	 * @return true if a user is signed in
	 */
	public boolean isSignedIn() {
		return currentUserId != null && currentIdToken != null;
	}

	/**
	 * Gets the current user ID.
	 * 
	 * @return the user ID, or null if not signed in
	 */
	public String getCurrentUserId() {
		return currentUserId;
	}

	/**
	 * Gets the current ID token.
	 * 
	 * @return the ID token, or null if not signed in
	 */
	public String getCurrentIdToken() {
		return currentIdToken;
	}

	/**
	 * Gets the current refresh token.
	 * 
	 * @return the refresh token, or null if not signed in
	 */
	public String getCurrentRefreshToken() {
		return currentRefreshToken;
	}

	/**
	 * Checks if the current token is expired.
	 * 
	 * @return true if the token is expired
	 */
	public boolean isTokenExpired() {
		if (tokenExpirationTime == 0) {
			return true;
		}
		return System.currentTimeMillis() >= tokenExpirationTime;
	}

	/**
	 * Updates the ID token after refresh.
	 * 
	 * @param newIdToken the new ID token
	 * @param expiresIn  token expiration time in seconds
	 */
	public void updateIdToken(String newIdToken, long expiresIn) {
		this.currentIdToken = newIdToken;
		this.tokenExpirationTime = System.currentTimeMillis() + (expiresIn * 1000);
	}
}
