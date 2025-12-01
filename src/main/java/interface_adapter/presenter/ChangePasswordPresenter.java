package interface_adapter.presenter;

import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.SettingsState;
import interface_adapter.view_model.SettingsViewModel;
import interface_adapter.view_model.ViewManagerModel;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.change_password.ChangePasswordOutputData;

/**
 * The Presenter for the Change Password Use Case.
 */
public class ChangePasswordPresenter implements ChangePasswordOutputBoundary {

    private final DashboardViewModel dashboardViewModel;
    private final SettingsViewModel settingsViewModel;
    private final ViewManagerModel viewManagerModel;

    public ChangePasswordPresenter(ViewManagerModel viewManagerModel,
            DashboardViewModel dashboardViewModel, SettingsViewModel settingsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.dashboardViewModel = dashboardViewModel;
        this.settingsViewModel = settingsViewModel;
    }

    @Override
    public void prepareSuccessView(ChangePasswordOutputData outputData) {
        // Clear any errors and password fields
        final SettingsState settingsState = settingsViewModel.getState();
        settingsState.setOldPassword("");
        settingsState.setNewPassword("");
        settingsState.setConfirmPassword("");
        settingsState.setChangePasswordError("");
        settingsViewModel.setState(settingsState);
        settingsViewModel.firePropertyChange();

        // Navigate to dashboard
        viewManagerModel.setState(dashboardViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        final SettingsState settingsState = settingsViewModel.getState();
        settingsState.setChangePasswordError(error);
        settingsViewModel.setState(settingsState);
        settingsViewModel.firePropertyChange();
    }
}
