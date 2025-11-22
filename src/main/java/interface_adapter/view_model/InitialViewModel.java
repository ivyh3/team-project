package interface_adapter.view_model;

/**
 * The View Model for the Login View.
 */
public class InitialViewModel extends ViewModel<LoginState> {

    public InitialViewModel() {
        super("log in");
        setState(new LoginState());
    }

}