package view;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract class for a View. Views are named JPanels.
 */
public abstract class View extends JPanel {
    protected String viewName;
    private static final int PADDING = 20;
    public View(String viewName) {
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
