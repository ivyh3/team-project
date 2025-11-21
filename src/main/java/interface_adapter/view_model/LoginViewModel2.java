package interface_adapter.view_model;

/**
 * The View Model for the Login View.
 */
public class LoginViewModel2 extends ViewModel<LoginState> {

    public LoginViewModel2() {
        super("log in");
        setState(new LoginState());
    }

}
