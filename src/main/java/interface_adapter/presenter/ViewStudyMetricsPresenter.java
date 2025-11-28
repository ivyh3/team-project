package interface_adapter.presenter;

import interface_adapter.view_model.MetricsViewModel;
import use_case.view_study_metrics.ViewStudyMetricsOutputBoundary;
import use_case.view_study_metrics.ViewStudyMetricsOutputData;

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
		viewModel.setDailyStudyDurations(outputData.getDailyStudyDurations());
		viewModel.setStartDate(outputData.getStartDate());
		viewModel.setAverageQuizScores(outputData.getAverageQuizScores());
		viewModel.setErrorMessage("");
	}

	@Override
	public void prepareFailView(String error) {
		viewModel.setErrorMessage(error);
	}
}