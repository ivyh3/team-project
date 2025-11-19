package interface_adapter.login;

import interface_adapter.ViewModel;

/**
 * The View Model for the Login View.
 */
public class InitialViewModel extends ViewModel<LoginState> {

    public InitialViewModel() {
        super("log in");
        setState(new LoginState());
    }

}