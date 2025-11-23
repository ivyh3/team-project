package interface_adapter.view_model;

/**
 * The View Model for the Logged In View.
 */
public class LoggedInViewModel extends ViewModel<DashboardState> {

    public LoggedInViewModel() {
        super("logged in");
        setState(new DashboardState());
    }

}
