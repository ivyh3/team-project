package interface_adapter.presenter;

import interface_adapter.view_model.ViewManagerModel;
import interface_adapter.view_model.DashboardState;
import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.LoginState;
import interface_adapter.view_model.LoginViewModel;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;

/**
 * The Presenter for the Logout Use Case.
 */
public class LogoutPresenter implements LogoutOutputBoundary {

    private DashboardViewModel dashboardViewModel;
    private ViewManagerModel viewManagerModel;
    private LoginViewModel loginViewModel;

    public LogoutPresenter(ViewManagerModel viewManagerModel,
            DashboardViewModel dashboardViewModel,
            LoginViewModel loginViewModel) {
        this.dashboardViewModel = dashboardViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(LogoutOutputData response) {
        // Clear the dashboard state
        final DashboardState dashboardState = dashboardViewModel.getState();
        dashboardState.setUserId("");
        dashboardState.setEmail("");
        dashboardViewModel.setState(dashboardState);
        dashboardViewModel.firePropertyChange();

        // Switch to the login view
        this.viewManagerModel.setState(loginViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }
}
