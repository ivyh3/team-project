package view;

import javax.swing.*;
import java.awt.*;

/**
 * Main view of the application after login.
 * Contains navigation and main content area.
 */
public class MainView extends JFrame {
	private JPanel contentPanel;
	private JButton startSessionButton;
	private JButton viewMetricsButton;
	private JButton uploadMaterialsButton;
	private JButton scheduleSessionButton;

	public MainView() {
		setTitle("AI Study Companion");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);

		initializeComponents();
		layoutComponents();

		setLocationRelativeTo(null);
	}

	private void initializeComponents() {
		contentPanel = new JPanel();
		contentPanel.setLayout(new CardLayout());

		startSessionButton = new JButton("Start Study Session");
		viewMetricsButton = new JButton("View Metrics");
		uploadMaterialsButton = new JButton("Upload Materials");
		scheduleSessionButton = new JButton("Schedule Session");
	}

	private void layoutComponents() {
		// TODO: Create a proper layout with navigation panel and content area
		setLayout(new BorderLayout());

		// Navigation panel (left side)
		JPanel navPanel = new JPanel();
		navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
		navPanel.add(startSessionButton);
		navPanel.add(viewMetricsButton);
		navPanel.add(uploadMaterialsButton);
		navPanel.add(scheduleSessionButton);

		add(navPanel, BorderLayout.WEST);
		add(contentPanel, BorderLayout.CENTER);
	}

	public void showPanel(String panelName) {
		CardLayout layout = (CardLayout) contentPanel.getLayout();
		layout.show(contentPanel, panelName);
	}

	public void addPanel(JPanel panel, String name) {
		contentPanel.add(panel, name);
	}
}
