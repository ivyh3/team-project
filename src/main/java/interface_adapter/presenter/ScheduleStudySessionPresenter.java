package interface_adapter.presenter;

import interface_adapter.view_model.ScheduleSessionViewModel;
import use_case.schedule_study_session.ScheduleStudySessionOutputBoundary;
import use_case.schedule_study_session.ScheduleStudySessionOutputData;

/**
 * Presenter for the Schedule Study Session use case.
 * Formats output data and updates the ScheduleSessionViewModel.
 */
public class ScheduleStudySessionPresenter implements ScheduleStudySessionOutputBoundary {
	private final ScheduleSessionViewModel viewModel;
	
	public ScheduleStudySessionPresenter(ScheduleSessionViewModel viewModel) {
		this.viewModel = viewModel;
	}
	
	@Override
	public void prepareSuccessView(ScheduleStudySessionOutputData outputData) {
		// Format the scheduled session information for display
		String sessionDescription = String.format("Session scheduled: %s to %s%s",
				outputData.getStartTime().toLocalTime().toString(),
				outputData.getEndTime().toLocalTime().toString(),
				outputData.getCalendarEventId() != null ? " (synced to Google Calendar)" : "");
		
		// Add the session to the list
		viewModel.addScheduledSession(sessionDescription);
		viewModel.setStatusMessage("Session scheduled successfully!");
		viewModel.setErrorMessage("");
		viewModel.setScheduleInProgress(false);
	}
	
	@Override
	public void prepareFailView(String error) {
		viewModel.setErrorMessage(error);
		viewModel.setStatusMessage("");
		viewModel.setScheduleInProgress(false);
	}
}

