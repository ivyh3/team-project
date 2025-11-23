package use_case.login;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final String email;

    public LoginOutputData(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
