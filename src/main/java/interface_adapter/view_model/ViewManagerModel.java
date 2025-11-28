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
     * Sets the currently active view. Will automatically fire property change.
     * 
     * @param viewName The view name of the now active view.
     */
    public void setView(String viewName) {
        this.setState(viewName);

        // I cannot think of any case where we set the view and NOT navigate to it,
        // basically, setting state => will fire property change so merged into one
        // method that makes it clear what the ViewManagerModel is doing.
        firePropertyChange();
    }
}
