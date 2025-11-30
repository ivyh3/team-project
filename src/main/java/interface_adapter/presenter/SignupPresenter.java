package interface_adapter.presenter;

import interface_adapter.view_model.*;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

/**
 * The Presenter for the Signup Use Case.
 */
public class SignupPresenter implements SignupOutputBoundary {

    private final SignupViewModel signupViewModel;
    private final DashboardViewModel dashboardViewModel;
    private final ViewManagerModel viewManagerModel;

    public SignupPresenter(ViewManagerModel viewManagerModel,
            SignupViewModel signupViewModel,
            DashboardViewModel dashboardViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.signupViewModel = signupViewModel;
        this.dashboardViewModel = dashboardViewModel;
    }

    @Override
    public void prepareSuccessView(SignupOutputData response) {
        // On success, automatically log in the user and go to dashboard
        final DashboardState dashboardState = dashboardViewModel.getState();
        dashboardState.setUserId(response.getUserId());
        dashboardState.setEmail(response.getEmail());
        dashboardViewModel.setState(dashboardState);
        dashboardViewModel.firePropertyChange();

        // Clear signup form fields
        signupViewModel.setState(new SignupState());
        signupViewModel.firePropertyChange();

        // Switch to the dashboard view
        viewManagerModel.setState(dashboardViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        final SignupState signupState = signupViewModel.getState();
        signupState.setEmailError(error);
        signupViewModel.setState(signupState);
        signupViewModel.firePropertyChange();
    }
}
