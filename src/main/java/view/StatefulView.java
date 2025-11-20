package view;

import interface_adapter.view_model.ViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Abstract class for a view which relies on state. A StatefulView will have a ViewModel which holds the state
 * that the StatefulView will rely on, and will listen to any changes in its ViewModel.
 * @param <T> The type of state that the viewModel should hold.
 */
public abstract class StatefulView<T> extends View implements PropertyChangeListener {
    protected ViewModel<T> viewModel;
    public StatefulView(String viewName, ViewModel<T> viewModel) {
        super(viewName);
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}