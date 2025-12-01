package use_case.logout;

/**
 * The Logout Interactor.
 */
public class LogoutInteractor implements LogoutInputBoundary {
    private LogoutOutputBoundary logoutPresenter;

    public LogoutInteractor(LogoutOutputBoundary logoutOutputBoundary) {
        // userDataAccessObject not needed - logout is purely client-side state clearing
        this.logoutPresenter = logoutOutputBoundary;
    }

    @Override
    public void execute() {
        // No server-side action needed for logout
        // Just notify the presenter to clear state and navigate
        LogoutOutputData outputData = new LogoutOutputData();
        logoutPresenter.prepareSuccessView(outputData);
    }
}
