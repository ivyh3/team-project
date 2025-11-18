package view;


import interface_adapter.view_model.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ViewManager implements PropertyChangeListener {
    private final ViewManagerModel viewManagerModel;
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