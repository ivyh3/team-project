package interface_adapter.view_model;

/**
 * The State information representing the logged-in user.
 */
public class DashboardState {
    private String username = "";
    private String password = "";
    private String passwordError;

    public DashboardState(DashboardState copy) {
        username = copy.username;
        password = copy.password;
        passwordError = copy.passwordError;
    }

    // Because of the previous copy constructor, the default constructor must be
    // explicit.
    public DashboardState() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getPasswordError() {
        return passwordError;
    }
}
