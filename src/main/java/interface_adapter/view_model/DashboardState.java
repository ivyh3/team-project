package interface_adapter.view_model;

/**
 * The State information representing the logged-in user and dashboard data.
 */
public class DashboardState {
    // I do not care at the moment.
    public static String userId = "";
    private String email = "";

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
