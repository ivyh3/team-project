package interface_adapter.presenter;

import interface_adapter.view_model.StudySessionViewModel;
import use_case.start_study_session.StartStudySessionOutputBoundary;
import use_case.start_study_session.StartStudySessionOutputData;

/**
 * Presenter for the Start Study Session use case.
 * Formats output data and updates the StudySessionViewModel.
 */
public class StartStudySessionPresenter implements StartStudySessionOutputBoundary {
	private final StudySessionViewModel viewModel;
	
	public StartStudySessionPresenter(StudySessionViewModel viewModel) {
		this.viewModel = viewModel;
	}
	
	@Override
	public void prepareSuccessView(StartStudySessionOutputData outputData) {
		// Update the view model with formatted data
		viewModel.setSessionId(outputData.getSessionId());
		viewModel.setCourseName(outputData.getCourseId());
		viewModel.setSessionActive(true);
		viewModel.setStatusMessage("Study session started successfully!");
		viewModel.setErrorMessage("");
		
		// Format the start time for display
		String formattedTime = outputData.getStartTime().toLocalTime().toString();
		viewModel.setTimerDisplay(formattedTime);
	}
	
	@Override
	public void prepareFailView(String error) {
		// Update the view model with error message
		viewModel.setSessionActive(false);
		viewModel.setErrorMessage(error);
		viewModel.setStatusMessage("");
	}
}

