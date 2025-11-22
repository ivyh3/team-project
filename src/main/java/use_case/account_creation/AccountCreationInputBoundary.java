package use_case.account_creation;

/**
 * Input boundary for the Account Creation use case.
 */
public interface AccountCreationInputBoundary {
    /**
     * Executes the account creation use case.
     * 
     * @param inputData the input data for creating an account
     */
    void execute(AccountCreationInputData inputData);
}
