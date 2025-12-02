package view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * Abstract class for a View. Views are named JPanels.
 */
public abstract class View extends JPanel {
    private final String viewName;

    public View(String viewName) {
        this.viewName = viewName;
        this.setName(viewName);
        setLayout(new BorderLayout());
    }

    /**
     * Get the view name for this view.
     *
     * @return The view name for this view.
     */
    public String getViewName() {
        return viewName;
    }

    public void onViewShown() {
    }
}
