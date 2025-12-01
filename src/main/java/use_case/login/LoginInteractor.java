package use_case.login;

import entity.User;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
            LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String email = loginInputData.getEmail();
        final String password = loginInputData.getPassword();

        if ("".equals(email)) {
            loginPresenter.prepareFailView("Email cannot be empty");
        }
        else if ("".equals(password)) {
            loginPresenter.prepareFailView("Password cannot be empty");
        }
        else if (!userDataAccessObject.existsByEmail(email)) {
            loginPresenter.prepareFailView(email + ": Account does not exist.");
        }
        else if (!userDataAccessObject.verifyPassword(email, password)) {
            loginPresenter.prepareFailView("Incorrect password for \"" + email + "\".");
        }
        else {
            final User user = userDataAccessObject.getUserByEmail(email);

            final LoginOutputData loginOutputData = new LoginOutputData(user.getUserId(), user.getEmail());
            loginPresenter.prepareSuccessView(loginOutputData);
        }
    }
}
