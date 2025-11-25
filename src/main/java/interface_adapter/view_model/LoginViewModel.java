package interface_adapter.view_model;

/**
 * ViewModel for the Login view.
 * Stores the state and data that the login view needs to display.
 */
public class LoginViewModel extends ViewModel<LoginState> {

    public LoginViewModel() {
        super("log in");
        setState(new LoginState());
    }
}
