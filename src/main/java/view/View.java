package view;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Abstract class for a View. Views are named JPanels.
 */
public abstract class View extends JPanel {
    private static final int PADDING = 20;
    private final String viewName;

    protected View(String viewName) {
        this.viewName = viewName;

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
    }

    /**
     * Get the view name for this view.
     *
     * @return The view name for this view.
     */
    public String getViewName() {
        return viewName;
    }
}
