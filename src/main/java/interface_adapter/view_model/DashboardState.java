package interface_adapter.view_model;

/**
 * The State information representing the logged-in user and dashboard data.
 */
public class DashboardState {
//    private String userId = "";
//    private String email = "";
    // TODO: Temporary values
    private String userId = "Z938NO5NfTd2XMX3ND5Jmqrnn6B2";
    private String email = "abc123@gmail.com";

    public DashboardState(DashboardState copy) {
        userId = copy.userId;
        email = copy.email;
    }

    // Because of the previous copy constructor, the default constructor must be
    // explicit.
    public DashboardState() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
