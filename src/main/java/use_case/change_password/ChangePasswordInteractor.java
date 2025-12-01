package use_case.change_password;

/**
 * The Change Password Interactor.
 */
public class ChangePasswordInteractor implements ChangePasswordInputBoundary {
    private final ChangePasswordUserDataAccessInterface userDataAccessObject;
    private final ChangePasswordOutputBoundary userPresenter;

    public ChangePasswordInteractor(ChangePasswordUserDataAccessInterface changePasswordDataAccessInterface,
            ChangePasswordOutputBoundary changePasswordOutputBoundary) {
        this.userDataAccessObject = changePasswordDataAccessInterface;
        this.userPresenter = changePasswordOutputBoundary;
    }

    @Override
    public void execute(ChangePasswordInputData changePasswordInputData) {
        final String userId = changePasswordInputData.getUserId();
        final String oldPassword = changePasswordInputData.getOldPassword();
        final String newPassword = changePasswordInputData.getNewPassword();
        final String confirmPassword = changePasswordInputData.getConfirmPassword();

        // Validate inputs
        if ("".equals(oldPassword)) {
            userPresenter.prepareFailView("Current password cannot be empty");
        }
        else if ("".equals(newPassword)) {
            userPresenter.prepareFailView("New password cannot be empty");
        }
        else if ("".equals(confirmPassword)) {
            userPresenter.prepareFailView("Confirm password cannot be empty");
        }
        else if (!newPassword.equals(confirmPassword)) {
            userPresenter.prepareFailView("New passwords do not match");
        }
        else if (newPassword.length() < 6) {
            userPresenter.prepareFailView("New password must be at least 6 characters");
        }
        else if (oldPassword.equals(newPassword)) {
            userPresenter.prepareFailView("New password must be different from current password");
        }
        else {
            // Get user's email to verify old password
            final String email = userDataAccessObject.getEmailByUserId(userId);

            // Verify the old password
            if (!userDataAccessObject.verifyPassword(email, oldPassword)) {
                userPresenter.prepareFailView("Current password is incorrect");
            }
            else {
                // Update to new password
                userDataAccessObject.changePassword(userId, newPassword);

                userPresenter.prepareSuccessView(new ChangePasswordOutputData());
            }
        }
    }
}
