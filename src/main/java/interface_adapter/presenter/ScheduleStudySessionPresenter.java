package interface_adapter.presenter;

import interface_adapter.view_model.ScheduleSessionViewModel;
import interface_adapter.view_model.ScheduleSessionState;
import use_case.schedule_study_session.ScheduleStudySessionOutputBoundary;
import use_case.schedule_study_session.ScheduleStudySessionOutputData;
import use_case.schedule_study_session.DeleteScheduledSessionOutputData;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import entity.ScheduledSession;
import java.util.List;

/**
 * Presenter for the Schedule Study Session use case.
 * Formats output data and updates the ScheduleSessionViewModel.
 */
public class ScheduleStudySessionPresenter implements ScheduleStudySessionOutputBoundary {
    private final ScheduleSessionViewModel viewModel;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ScheduleStudySessionPresenter(ScheduleSessionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ScheduleStudySessionOutputData outputData) {

        LocalDateTime startTime = LocalDateTime.parse(outputData.getStartTime());
        LocalDateTime endTime = LocalDateTime.parse(outputData.getEndTime());

        ScheduleSessionState displayData = new ScheduleSessionState(
                outputData.getId(),
                outputData.getTitle(),
                startTime,
                endTime);

        // 💡 FIX 2: Add the newly scheduled session (with its permanent ID) to the ViewModel.
        viewModel.addScheduledSession(displayData);

        viewModel.setStatusMessage("Session scheduled successfully!");
        viewModel.setErrorMessage("");
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setErrorMessage(error);
        viewModel.setStatusMessage("");
    }

    @Override
    public void prepareDeleteSuccessView(DeleteScheduledSessionOutputData outputData) {

        String deletedId = outputData.getDeletedSessionId();

        // The View already handles the optimistic delete, but the Presenter confirms it
        // and ensures the official ID is removed.
        viewModel.removeScheduledSessionById(deletedId);

        viewModel.setStatusMessage(outputData.getStatusMessage());
        viewModel.setErrorMessage("");
    }

    /**
     * Handles the initial data load from the Interactor.
     * Converts the ScheduledSession Entities into ScheduleSessionState DTOs
     * and updates the ViewModel.
     * * 💡 FIX 1: Renamed from loadSessions to presentSessions to satisfy the interface.
     */
    @Override
    public void loadSessions(List<ScheduledSession> sessions) {

        viewModel.clearSessions();
        for (ScheduledSession session : sessions) {

            ScheduleSessionState state = new ScheduleSessionState(
                    session.getId(),
                    session.getTitle(),
                    session.getStartTime(),
                    session.getEndTime());

            // This method will also fire the property change event
            viewModel.addScheduledSession(state);
        }
        viewModel.setErrorMessage("");
    }

}