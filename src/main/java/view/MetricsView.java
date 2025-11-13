package view;

import javax.swing.*;
import java.awt.*;

/**
 * View for displaying study metrics and statistics.
 */
public class MetricsView extends JPanel {
	private JComboBox<String> courseFilter;
	private JComboBox<String> timeFilter;
	private JPanel chartsPanel;
	private JLabel averageStudyTimeLabel;
	private JLabel averageScoreLabel;
	private JLabel mostStudiedSubjectLabel;

	public MetricsView() {
		initializeComponents();
		layoutComponents();
	}

	private void initializeComponents() {
		courseFilter = new JComboBox<>(new String[] { "All Courses", "CSC207", "MAT137" });
		timeFilter = new JComboBox<>(new String[] { "This Week", "This Month", "All Time" });
		chartsPanel = new JPanel();
		chartsPanel.setLayout(new GridLayout(2, 1));

		averageStudyTimeLabel = new JLabel("Average Weekly Study Time: --");
		averageScoreLabel = new JLabel("Average Quiz Score: --");
		mostStudiedSubjectLabel = new JLabel("Most Studied Subject: --");
	}

	private void layoutComponents() {
		setLayout(new BorderLayout());

		// TODO: Properly layout the metrics components
		// Top: Filters
		// Middle: Charts (study duration over time, quiz scores)
		// Bottom: Summary statistics

		JPanel filterPanel = new JPanel();
		filterPanel.add(new JLabel("Course:"));
		filterPanel.add(courseFilter);
		filterPanel.add(new JLabel("Time:"));
		filterPanel.add(timeFilter);

		add(filterPanel, BorderLayout.NORTH);
		add(chartsPanel, BorderLayout.CENTER);

		JPanel statsPanel = new JPanel();
		statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
		statsPanel.add(averageStudyTimeLabel);
		statsPanel.add(averageScoreLabel);
		statsPanel.add(mostStudiedSubjectLabel);
		add(statsPanel, BorderLayout.SOUTH);
	}

	public void updateMetrics(String avgStudyTime, String avgScore, String mostStudied) {
		averageStudyTimeLabel.setText("Average Weekly Study Time: " + avgStudyTime);
		averageScoreLabel.setText("Average Quiz Score: " + avgScore);
		mostStudiedSubjectLabel.setText("Most Studied Subject: " + mostStudied);
	}
}
