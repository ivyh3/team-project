package interface_adapter.view_model;

/**
 * The View Model for the Settings View.
 */
public class SettingsViewModel extends ViewModel<SettingsState> {

    public SettingsViewModel() {
        super("settings");
        setState(new SettingsState());
    }
}

