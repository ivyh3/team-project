package use_case.change_password;

/**
 * The input data for the Change Password Use Case.
 */
public class ChangePasswordInputData {

    private final String userId;
    private final String oldPassword;
    private final String newPassword;
    private final String confirmPassword;

    public ChangePasswordInputData(String userId, String oldPassword, String newPassword, String confirmPassword) {
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    String getUserId() {
        return userId;
    }

    String getOldPassword() {
        return oldPassword;
    }

    String getNewPassword() {
        return newPassword;
    }

    String getConfirmPassword() {
        return confirmPassword;
    }

}
