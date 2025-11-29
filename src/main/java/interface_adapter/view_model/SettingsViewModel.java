package interface_adapter.view_model;

public class SettingsViewModel extends ViewModel<Boolean> {
    public SettingsViewModel() {
        super("settings");
        this.setState(Boolean.FALSE);
    }
}