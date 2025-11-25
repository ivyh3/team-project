package interface_adapter.view_model;

/**
 * The View Model for the Dashboard View.
 * Stores the state and data that the dashboard view needs to display.
 */
public class DashboardViewModel extends ViewModel<DashboardState> {

    public DashboardViewModel() {
        super("dashboard");
        setState(new DashboardState());
    }
}

