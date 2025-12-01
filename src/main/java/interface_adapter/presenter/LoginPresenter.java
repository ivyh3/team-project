package interface_adapter.presenter;

import interface_adapter.view_model.*;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final DashboardViewModel dashboardViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
            DashboardViewModel dashboardViewModel,
            LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.dashboardViewModel = dashboardViewModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, update the dashboardViewModel's state with user info
        final DashboardState dashboardState = dashboardViewModel.getState();
        dashboardState.setUserId(response.getUserId());
        dashboardState.setEmail(response.getEmail());
        dashboardViewModel.setState(dashboardState);
        this.dashboardViewModel.firePropertyChange();

        // Clear everything from the LoginViewModel's state
        loginViewModel.setState(new LoginState());
        loginViewModel.firePropertyChange();

        // Switch to the dashboard view
        this.viewManagerModel.setState(dashboardViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setEmailError(error);
        loginViewModel.setState(loginState);
        loginViewModel.firePropertyChange();
    }
}
