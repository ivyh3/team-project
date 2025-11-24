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
		viewModel.setAverageWeeklyStudyTime(outputData.getAverageWeeklyStudyTime());
		viewModel.setDailyStudyDurations(outputData.getDailyStudyDurations());
		viewModel.setStartDate(outputData.getStartDate());
		viewModel.setAverageQuizScores(outputData.getAverageQuizScores());

		// Set most studied subject
//		viewModel.setMostStudiedSubject(outputData.getMostStudiedSubject());

		viewModel.setErrorMessage("");
	}

	@Override
	public void prepareFailView(String error) {
		viewModel.setErrorMessage(error);
	}
}