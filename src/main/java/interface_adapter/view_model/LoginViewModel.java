package interface_adapter.view_model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * ViewModel for the Login view.
 * Stores the state and data that the login view needs to display.
 */
public class LoginViewModel extends ViewModel<LoginState> {
    private final PropertyChangeSupport support;

    private String errorMessage;
    private boolean loginInProgress;
    private String lastAttemptedEmail;

    public LoginViewModel() {
        super("log in");
        setState(new LoginState());

        this.support = new PropertyChangeSupport(this);
        this.errorMessage = "";
        this.loginInProgress = false;
        this.lastAttemptedEmail = "";
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        String oldValue = this.errorMessage;
        this.errorMessage = errorMessage;
        support.firePropertyChange("errorMessage", oldValue, errorMessage);
    }

    public boolean isLoginInProgress() {
        return loginInProgress;
    }

    public void setLoginInProgress(boolean loginInProgress) {
        boolean oldValue = this.loginInProgress;
        this.loginInProgress = loginInProgress;
        support.firePropertyChange("loginInProgress", oldValue, loginInProgress);
    }

    public String getLastAttemptedEmail() {
        return lastAttemptedEmail;
    }

    public void setLastAttemptedEmail(String lastAttemptedEmail) {
        String oldValue = this.lastAttemptedEmail;
        this.lastAttemptedEmail = lastAttemptedEmail;
        support.firePropertyChange("lastAttemptedEmail", oldValue, lastAttemptedEmail);
    }
}

