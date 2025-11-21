package interface_adapter.presenter;

import interface_adapter.view_model.LoginViewModel2;
import use_case.account_creation.AccountCreationOutputBoundary;
import use_case.account_creation.AccountCreationOutputData;

/**
 * Presenter for the Account Creation use case.
 * Formats output data and updates the LoginViewModel.
 */
public class AccountCreationPresenter implements AccountCreationOutputBoundary {
	private final LoginViewModel2 viewModel;
	
	public AccountCreationPresenter(LoginViewModel2 viewModel) {
		this.viewModel = viewModel;
	}
	
	@Override
	public void prepareSuccessView(AccountCreationOutputData outputData) {
		// Clear any error messages
		viewModel.setErrorMessage("");
		viewModel.setLoginInProgress(false);
		viewModel.setLastAttemptedEmail(outputData.getEmail());
		
		// At this point, the view should transition to the main application
		// The actual transition logic will be handled by the view
	}
	
	@Override
	public void prepareFailView(String error) {
		viewModel.setErrorMessage(error);
		viewModel.setLoginInProgress(false);
	}
}

