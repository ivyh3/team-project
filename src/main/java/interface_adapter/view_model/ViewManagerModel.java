package interface_adapter.view_model;

/**
 * Model for the View Manager. Its state is the currently active view name.
 * Initial state is the empty string.
 */
public class ViewManagerModel extends ViewModel<String> {
    public ViewManagerModel() {
        super("viewManager");
        this.setState("");
    }

    /**
     * Sets the state (active view name).
     * 
     * @param viewName The view name of the now active view.
     */
    public void setView(String viewName) {
        this.setState(viewName);
        // this.firePropertyChange();
    }
}
