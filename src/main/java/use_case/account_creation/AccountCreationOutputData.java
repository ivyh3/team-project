package use_case.account_creation;

/**
 * Output data for the Account Creation use case.
 */
public class AccountCreationOutputData {
	private final String userId;
	private final String email;
	private final String name;

	public AccountCreationOutputData(String userId, String email, String name) {
		this.userId = userId;
		this.email = email;
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}
}
