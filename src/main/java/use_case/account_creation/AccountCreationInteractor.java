package use_case.account_creation;

import entity.User;
import interface_adapter.repository.UserRepository;
import frameworks_drivers.firebase.FirebaseUserDataAccessObject;

import java.time.LocalDateTime;

/**
 * Interactor for the Account Creation use case.
 * Coordinates Firebase Authentication and Firestore data persistence.
 */
public class AccountCreationInteractor implements AccountCreationInputBoundary {
	private final UserRepository userRepository;
	private final FirebaseUserDataAccessObject authService;
	private final AccountCreationOutputBoundary outputBoundary;

	public AccountCreationInteractor(UserRepository userRepository,
			FirebaseUserDataAccessObject authService,
			AccountCreationOutputBoundary outputBoundary) {
		this.userRepository = userRepository;
		this.authService = authService;
		this.outputBoundary = outputBoundary;
	}

	@Override
	public void execute(AccountCreationInputData inputData) {
		// 1. Validate input
		if (inputData.getPassword().length() < 6) {
			outputBoundary.prepareFailView("Password must be at least 6 characters");
			return;
		}

		// 2. Check if email already exists in Firestore
		if (userRepository.existsByEmail(inputData.getEmail())) {
			outputBoundary.prepareFailView("Email already in use");
			return;
		}

		// 3. Create Firebase Auth account
		String userId = authService.createUserWithEmail(
				inputData.getEmail(),
				inputData.getPassword());

		if (userId == null) {
			outputBoundary.prepareFailView("Failed to create authentication account");
			return;
		}

		// 4. Create User entity and save to Firestore
		User newUser = new User(
				userId,
				inputData.getName(),
				inputData.getEmail(),
				LocalDateTime.now());

		userRepository.save(newUser);

		// 5. Prepare success view
		AccountCreationOutputData outputData = new AccountCreationOutputData(
				userId,
				inputData.getName(),
				inputData.getEmail());
		outputBoundary.prepareSuccessView(outputData);
	}
}
