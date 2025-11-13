package view;

import interface_adapter.view_model.StudySessionViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for starting and managing a study session.
 * Observes the StudySessionViewModel and updates the UI accordingly.
 */
public class StudySessionView extends JPanel implements PropertyChangeListener {
	private final StudySessionViewModel viewModel;
	
	private JComboBox<String> courseSelector;
	private JTextArea promptArea;
	private JList<String> referenceMaterialsList;
	private JButton startTimerButton;
	private JButton startStopwatchButton;
	private JButton endSessionButton;
	private JLabel timerLabel;
	private JLabel statusLabel;
	private JLabel errorLabel;
	private JSpinner durationSpinner;
	
	public StudySessionView(StudySessionViewModel viewModel) {
		this.viewModel = viewModel;
		this.viewModel.addPropertyChangeListener(this);
		
		initializeComponents();
		layoutComponents();
	}
	
	private void initializeComponents() {
		courseSelector = new JComboBox<>();
		promptArea = new JTextArea(5, 30);
		promptArea.setLineWrap(true);
		referenceMaterialsList = new JList<>();
		startTimerButton = new JButton("Start Timer");
		startStopwatchButton = new JButton("Start Stopwatch");
		endSessionButton = new JButton("End Session");
		endSessionButton.setEnabled(false);
		
		timerLabel = new JLabel("00:00:00");
		timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
		
		statusLabel = new JLabel(" ");
		errorLabel = new JLabel(" ");
		errorLabel.setForeground(Color.RED);
		
		durationSpinner = new JSpinner(new SpinnerNumberModel(25, 1, 180, 5));
	}
	
	private void layoutComponents() {
		setLayout(new BorderLayout());
		
		// TODO: Properly layout the components
		// Top: Course selector
		// Middle: Prompt area, reference materials
		// Bottom: Timer controls and display, status and error labels
	}
	
	public String getSelectedCourse() {
		return (String) courseSelector.getSelectedItem();
	}
	
	public String getPrompt() {
		return promptArea.getText();
	}
	
	public void setStartTimerButtonListener(java.awt.event.ActionListener listener) {
		startTimerButton.addActionListener(listener);
	}
	
	public void setEndSessionButtonListener(java.awt.event.ActionListener listener) {
		endSessionButton.addActionListener(listener);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// Update view based on ViewModel changes
		switch (evt.getPropertyName()) {
			case "timerDisplay":
				timerLabel.setText(viewModel.getTimerDisplay());
				break;
			case "sessionActive":
				boolean active = viewModel.isSessionActive();
				startTimerButton.setEnabled(!active);
				startStopwatchButton.setEnabled(!active);
				endSessionButton.setEnabled(active);
				break;
			case "statusMessage":
				statusLabel.setText(viewModel.getStatusMessage());
				break;
			case "errorMessage":
				String error = viewModel.getErrorMessage();
				errorLabel.setText(error.isEmpty() ? " " : error);
				break;
			case "courseName":
				// Update course display if needed
				break;
		}
	}
}

