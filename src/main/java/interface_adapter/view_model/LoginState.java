package interface_adapter.view_model;

/**
 * The state for the Login View Model.
 */
public class LoginState {
    private String email = "";
    private String emailError;
    private String password = "";
    private String passwordError;

    public String getEmail() {
        return email;
    }

    public String getEmailError() {
        return emailError;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    @Override
    public String toString() {
        return "LoginState{"
                + "email='" + email + '\''
                + ", password='" + password + '\''
                + '}';
    }
}
