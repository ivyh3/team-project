package use_case.signup;

import utils.ValidationUtils;

import entity.User;
import entity.UserFactory;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
            SignupOutputBoundary signupOutputBoundary,
            UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignupInputData signupInputData) {
        final String email = signupInputData.getEmail();
        final String password = signupInputData.getPassword();

        if ("".equals(email)) {
            userPresenter.prepareFailView("Email cannot be empty");
        } else if (ValidationUtils.isValidEmail(email)) {
            userPresenter.prepareFailView("A valid email is required");
        } else if ("".equals(password)) {
            userPresenter.prepareFailView("Password cannot be empty");
        } else if (password.length() < 6) {
            userPresenter.prepareFailView("Password must be at least 6 characters");
        } else if (userDataAccessObject.existsByEmail(email)) {
            userPresenter.prepareFailView("User already exists");
        } else if (!password.equals(signupInputData.getRepeatPassword())) {
            userPresenter.prepareFailView("Passwords don't match");
        } else {
            userDataAccessObject.createUser(email, password);

            final SignupOutputData signupOutputData = new SignupOutputData(email);
            userPresenter.prepareSuccessView(signupOutputData);
        }
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}
