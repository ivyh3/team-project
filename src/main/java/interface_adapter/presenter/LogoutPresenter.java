package interface_adapter.presenter;

import interface_adapter.view_model.ViewManagerModel;
import interface_adapter.view_model.DashboardState;
import interface_adapter.view_model.DashboardViewModel;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;

/**
 * The Presenter for the Logout Use Case.
 */
public class LogoutPresenter implements LogoutOutputBoundary {

    private DashboardViewModel dashboardViewModel;
    private ViewManagerModel viewManagerModel;

    public LogoutPresenter(ViewManagerModel viewManagerModel,
            DashboardViewModel dashboardViewModel) {
        this.dashboardViewModel = dashboardViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(LogoutOutputData response) {
        // Clear the dashboard state
        final DashboardState dashboardState = dashboardViewModel.getState();
        dashboardState.setUserId("");
        dashboardState.setEmail("");
        dashboardViewModel.setState(dashboardState);
        dashboardViewModel.firePropertyChange();

        // Switch to the initial view (logged out state)
        this.viewManagerModel.setState("initial");
        this.viewManagerModel.firePropertyChange();
    }
}
