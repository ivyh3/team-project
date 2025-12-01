package interface_adapter.controller;

import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInputData;

/**
 * Controller for the Change Password Use Case.
 */
public class ChangePasswordController {
    private final ChangePasswordInputBoundary userChangePasswordUseCaseInteractor;

    public ChangePasswordController(ChangePasswordInputBoundary userChangePasswordUseCaseInteractor) {
        this.userChangePasswordUseCaseInteractor = userChangePasswordUseCaseInteractor;
    }

    /**
     * Executes the Change Password Use Case.
     * 
     * @param userId          the user ID whose password to change
     * @param oldPassword     the current password
     * @param newPassword     the new password
     * @param confirmPassword the confirmation of the new password
     */
    public void execute(String userId, String oldPassword, String newPassword, String confirmPassword) {
        final ChangePasswordInputData changePasswordInputData = new ChangePasswordInputData(userId, oldPassword,
                newPassword, confirmPassword);

        userChangePasswordUseCaseInteractor.execute(changePasswordInputData);
    }
}
