package use_case.account_creation;

import interface_adapter.repository.UserRepository;
import frameworks_drivers.firebase.FirebaseAuthService;

/**
 * Interactor for the Account Creation use case.
 */
public class AccountCreationInteractor implements AccountCreationInputBoundary {
	private final UserRepository userRepository;
	private final FirebaseAuthService authService;
	private final AccountCreationOutputBoundary outputBoundary;

	public AccountCreationInteractor(UserRepository userRepository,
			FirebaseAuthService authService,
			AccountCreationOutputBoundary outputBoundary) {
		this.userRepository = userRepository;
		this.authService = authService;
		this.outputBoundary = outputBoundary;
	}

	@Override
	public void execute(AccountCreationInputData inputData) {
		// TODO: Implement the business logic for account creation
		// 1. Check if using Google auth or email/password
		// 2. Create account via Firebase Auth
		// 3. Create User entity and save to Firestore
		// 4. Prepare success or failure view
	}
}
