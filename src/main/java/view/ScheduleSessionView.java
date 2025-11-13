package view;

import javax.swing.*;
import java.awt.*;

/**
 * View for scheduling future study sessions.
 */
public class ScheduleSessionView extends JPanel {
	private JComboBox<String> courseSelector;
	private JSpinner dateSpinner;
	private JSpinner startTimeSpinner;
	private JSpinner endTimeSpinner;
	private JCheckBox syncCalendarCheckbox;
	private JButton scheduleButton;
	private JList<String> scheduledSessionsList;
	private DefaultListModel<String> listModel;

	public ScheduleSessionView() {
		initializeComponents();
		layoutComponents();
	}

	private void initializeComponents() {
		courseSelector = new JComboBox<>();

		// Date and time spinners
		dateSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
		dateSpinner.setEditor(dateEditor);

		startTimeSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "HH:mm");
		startTimeSpinner.setEditor(startTimeEditor);

		endTimeSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
		endTimeSpinner.setEditor(endTimeEditor);

		syncCalendarCheckbox = new JCheckBox("Sync with Google Calendar");
		scheduleButton = new JButton("Schedule Session");

		listModel = new DefaultListModel<>();
		scheduledSessionsList = new JList<>(listModel);
	}

	private void layoutComponents() {
		setLayout(new BorderLayout());

		// TODO: Properly layout the schedule components
		// Top: Course selector, date/time pickers, sync checkbox
		// Middle: List of scheduled sessions
		// Bottom: Schedule button

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(5, 2));
		formPanel.add(new JLabel("Course:"));
		formPanel.add(courseSelector);
		formPanel.add(new JLabel("Date:"));
		formPanel.add(dateSpinner);
		formPanel.add(new JLabel("Start Time:"));
		formPanel.add(startTimeSpinner);
		formPanel.add(new JLabel("End Time:"));
		formPanel.add(endTimeSpinner);
		formPanel.add(syncCalendarCheckbox);

		add(formPanel, BorderLayout.NORTH);
		add(new JScrollPane(scheduledSessionsList), BorderLayout.CENTER);
		add(scheduleButton, BorderLayout.SOUTH);
	}

	public void setScheduleButtonListener(java.awt.event.ActionListener listener) {
		scheduleButton.addActionListener(listener);
	}

	public void addScheduledSession(String sessionDescription) {
		listModel.addElement(sessionDescription);
	}
}
