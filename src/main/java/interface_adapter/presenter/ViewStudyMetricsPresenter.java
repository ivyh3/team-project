package interface_adapter.presenter;

import interface_adapter.view_model.MetricsViewModel;
import use_case.view_study_metrics.ViewStudyMetricsOutputBoundary;
import use_case.view_study_metrics.ViewStudyMetricsOutputData;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Presenter for the View Study Metrics use case.
 * Formats output data and updates the MetricsViewModel.
 */
public class ViewStudyMetricsPresenter implements ViewStudyMetricsOutputBoundary {
	private final MetricsViewModel viewModel;
	
	public ViewStudyMetricsPresenter(MetricsViewModel viewModel) {
		this.viewModel = viewModel;
	}
	
	@Override
	public void prepareSuccessView(ViewStudyMetricsOutputData outputData) {
		// Format average weekly study time
		Duration avgWeekly = outputData.getAverageWeeklyStudyTime();
		String formattedWeeklyTime = formatDuration(avgWeekly);
		viewModel.setAverageWeeklyStudyTime(formattedWeeklyTime);
		
		// Format daily study durations
		Map<String, String> formattedDurations = new HashMap<>();
		outputData.getDailyStudyDurations().forEach((date, duration) ->
				formattedDurations.put(date, formatDuration(duration)));
		viewModel.setDailyStudyDurations(formattedDurations);
		
		// Format course scores
		Map<String, String> formattedScores = new HashMap<>();
		outputData.getAverageQuizScores().forEach((course, score) ->
				formattedScores.put(course, String.format("%.1f%%", score)));
		viewModel.setCourseScores(formattedScores);
		
		// Set most studied subject
		viewModel.setMostStudiedSubject(outputData.getMostStudiedSubject());
		
		// Calculate overall average score
		if (!outputData.getAverageQuizScores().isEmpty()) {
			double avgScore = outputData.getAverageQuizScores().values().stream()
					.mapToDouble(Float::doubleValue)
					.average()
					.orElse(0.0);
			viewModel.setAverageQuizScore(String.format("%.1f%%", avgScore));
		} else {
			viewModel.setAverageQuizScore("--");
		}
		
		viewModel.setErrorMessage("");
	}
	
	@Override
	public void prepareFailView(String error) {
		viewModel.setErrorMessage(error);
	}
	
	/**
	 * Formats a Duration into a human-readable string.
	 */
	private String formatDuration(Duration duration) {
		if (duration == null) {
			return "0h 0m";
		}
		long hours = duration.toHours();
		long minutes = duration.toMinutes() % 60;
		return String.format("%dh %dm", hours, minutes);
	}
}

