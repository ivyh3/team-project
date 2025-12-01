package view;

import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import interface_adapter.view_model.ViewManagerModel;

/**
 * View Manager class that listens for changes in the active view and sets
 * the current view.
 */
public class ViewManager implements PropertyChangeListener {
    final ViewManagerModel viewManagerModel;
    private final CardLayout cardLayout;
    private final JPanel views;

    public ViewManager(JPanel views, CardLayout cardLayout, ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addPropertyChangeListener(this);
        this.cardLayout = cardLayout;
        this.views = views;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("state")) {
            System.out.println(event.getNewValue());

            final String viewModelName = (String) event.getNewValue();
            cardLayout.show(views, viewModelName);
        }
    }
}