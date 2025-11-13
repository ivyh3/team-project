package use_case.account_creation;

/**
 * Output boundary for the Account Creation use case.
 */
public interface AccountCreationOutputBoundary {
	/**
	 * Prepares the success view.
	 * 
	 * @param outputData the output data
	 */
	void prepareSuccessView(AccountCreationOutputData outputData);

	/**
	 * Prepares the failure view.
	 * 
	 * @param error the error message
	 */
	void prepareFailView(String error);
}
