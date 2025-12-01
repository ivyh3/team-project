package interface_adapter.view_model;

/**
 * The state for the Settings View Model.
 */
public class SettingsState {
    private String oldPassword = "";
    private String newPassword = "";
    private String confirmPassword = "";
    private String changePasswordError;

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getChangePasswordError() {
        return changePasswordError;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setChangePasswordError(String changePasswordError) {
        this.changePasswordError = changePasswordError;
    }

    @Override
    public String toString() {
        return "SettingsState{"
                + "oldPassword='" + oldPassword + '\''
                + ", newPassword='" + newPassword + '\''
                + ", confirmPassword='" + confirmPassword + '\''
                + '}';
    }
}
