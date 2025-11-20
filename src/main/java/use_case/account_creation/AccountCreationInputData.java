package use_case.account_creation;

/**
 * Input data for the Account Creation use case.
 */
public class AccountCreationInputData {
	private final String email;
	private final String password;
	private final String name;
	private final boolean useGoogleAuth;
	private final String googleToken;

	public AccountCreationInputData(String email, String password, String name,
			boolean useGoogleAuth, String googleToken) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.useGoogleAuth = useGoogleAuth;
		this.googleToken = googleToken;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public boolean isUseGoogleAuth() {
		return useGoogleAuth;
	}

	public String getGoogleToken() {
		return googleToken;
	}
}
