package use_case.change_password;

/**
 * The DAO interface for the Change Password Use Case.
 */
public interface ChangePasswordUserDataAccessInterface {

    /**
     * Verifies the user's current password.
     * 
     * @param email    the user's email
     * @param password the password to verify
     * @return true if the password is correct, false otherwise
     */
    boolean verifyPassword(String email, String password);

    /**
     * Updates the user's password.
     * 
     * @param userId      the user ID whose password is to be updated
     * @param newPassword the new password
     */
    void changePassword(String userId, String newPassword);

    /**
     * Gets the email for a given user ID.
     * 
     * @param userId the user ID
     * @return the user's email
     */
    String getEmailByUserId(String userId);
}
