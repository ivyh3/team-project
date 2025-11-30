package interface_adapter.presenter;

import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.ViewManagerModel;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.change_password.ChangePasswordOutputData;

/**
 * The Presenter for the Change Password Use Case.
 */
public class ChangePasswordPresenter implements ChangePasswordOutputBoundary {

    private final DashboardViewModel dashboardViewModel;
    private final ViewManagerModel viewManagerModel;

    public ChangePasswordPresenter(ViewManagerModel viewManagerModel,
            DashboardViewModel dashboardViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.dashboardViewModel = dashboardViewModel;
    }

    @Override
    public void prepareSuccessView(ChangePasswordOutputData outputData) {
        // dashboardViewModel.getState().setPassword("");
        // dashboardViewModel.getState().setPasswordError(null);
        // dashboardViewModel.firePropertyChange("password");
    }

    @Override
    public void prepareFailView(String error) {
        // dashboardViewModel.getState().setPasswordError(error);
        // dashboardViewModel.firePropertyChange("password");
    }
}
