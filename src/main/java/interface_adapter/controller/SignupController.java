package interface_adapter.controller;

import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;

/**
 * Controller for the Signup Use Case.
 */
public class SignupController {

    private final SignupInputBoundary userSignupUseCaseInteractor;

    public SignupController(SignupInputBoundary userSignupUseCaseInteractor) {
        this.userSignupUseCaseInteractor = userSignupUseCaseInteractor;
    }

    /**
     * Executes the Signup Use Case.
     * 
     * @param email     the email to sign up
     * @param password1 the password
     * @param password2 the password repeated
     */
    public void execute(String email, String password1, String password2) {
        final SignupInputData signupInputData = new SignupInputData(
                email, password1, password2);

        userSignupUseCaseInteractor.execute(signupInputData);
    }
}
