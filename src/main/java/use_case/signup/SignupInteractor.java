package use_case.signup;

import entity.User;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary signupPresenter;

    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
            SignupOutputBoundary signupOutputBoundary) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.signupPresenter = signupOutputBoundary;
    }

    @Override
    public void execute(SignupInputData signupInputData) {
        final String email = signupInputData.getEmail();
        final String password = signupInputData.getPassword();

        if ("".equals(email)) {
            signupPresenter.prepareFailView("Email cannot be empty");
        }
        else if ("".equals(password)) {
            signupPresenter.prepareFailView("Password cannot be empty");
        }
        else if (password.length() < 6) {
            signupPresenter.prepareFailView("Password must be at least 6 characters");
        }
        else if (userDataAccessObject.existsByEmail(email)) {
            signupPresenter.prepareFailView("User already exists");
        }
        else if (!password.equals(signupInputData.getRepeatPassword())) {
            signupPresenter.prepareFailView("Passwords don't match");
        }
        else {
            userDataAccessObject.createUser(email, password);
            final User user = userDataAccessObject.getUser(email);

            final SignupOutputData signupOutputData = new SignupOutputData(user.getUserId(), user.getEmail());
            signupPresenter.prepareSuccessView(signupOutputData);
        }
    }
}
