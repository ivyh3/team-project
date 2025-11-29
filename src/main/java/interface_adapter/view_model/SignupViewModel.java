package interface_adapter.view_model;

/**
 * The ViewModel for the Signup View.
 */
public class SignupViewModel extends ViewModel<SignupState> {

    public static final String TITLE_LABEL = "Sign Up";
    public static final String EMAIL_LABEL = "Enter email";
    public static final String PASSWORD_LABEL = "Choose password";
    public static final String REPEAT_PASSWORD_LABEL = "Enter password again";

    public static final String SIGNUP_BUTTON_LABEL = "Sign Up";
    public static final String CANCEL_BUTTON_LABEL = "Return";

    public static final String TO_LOGIN_BUTTON_LABEL = "Go to Login";

    public SignupViewModel() {
        super("sign up");
        setState(new SignupState());
    }

}