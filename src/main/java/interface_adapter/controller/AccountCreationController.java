package interface_adapter.controller;

import use_case.account_creation.AccountCreationInputBoundary;
import use_case.account_creation.AccountCreationInputData;

/**
 * Controller for the Account Creation use case.
 */
public class AccountCreationController {
    private final AccountCreationInputBoundary interactor;
    
    public AccountCreationController(AccountCreationInputBoundary interactor) {
        this.interactor = interactor;
    }
    
    /**
     * Executes the account creation use case with email/password.
     * @param email the email
     * @param password the password
     * @param name the user's name
     */
    public void executeWithEmail(String email, String password, String name) {
        AccountCreationInputData inputData = new AccountCreationInputData(
            email, password, name, false, null
        );
        interactor.execute(inputData);
    }
    
    /**
     * Executes the account creation use case with Google auth.
     * @param googleToken the Google OAuth token
     * @param name the user's name
     */
    public void executeWithGoogle(String googleToken, String name) {
        AccountCreationInputData inputData = new AccountCreationInputData(
            null, null, name, true, googleToken
        );
        interactor.execute(inputData);
    }
}

