package interface_adapter.presenter;

import java.time.LocalDateTime;
import java.util.List;

import entity.ScheduledSession;
import interface_adapter.view_model.ScheduleSessionState;
import interface_adapter.view_model.ScheduleSessionViewModel;
import use_case.schedule_study_session.DeleteScheduledSessionOutputData;
import use_case.schedule_study_session.ScheduleStudySessionOutputBoundary;
import use_case.schedule_study_session.ScheduleStudySessionOutputData;

public class ScheduleStudySessionPresenter implements ScheduleStudySessionOutputBoundary {
    private final ScheduleSessionViewModel viewModel;

    public ScheduleStudySessionPresenter(ScheduleSessionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ScheduleStudySessionOutputData outputData) {

        final LocalDateTime startTime = LocalDateTime.parse(outputData.getStartTime());
        final LocalDateTime endTime = LocalDateTime.parse(outputData.getEndTime());

        final ScheduleSessionState displayData = new ScheduleSessionState(
                outputData.getId(),
                outputData.getTitle(),
                startTime,
                endTime);

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

        final String deletedId = outputData.getDeletedSessionId();

        viewModel.removeScheduledSessionById(deletedId);

        viewModel.setStatusMessage(outputData.getStatusMessage());
        viewModel.setErrorMessage("");
    }

    @Override
    public void loadSessions(List<ScheduledSession> sessions) {

        viewModel.clearSessions();
        for (ScheduledSession session : sessions) {

            final ScheduleSessionState state = new ScheduleSessionState(
                    session.getId(),
                    session.getTitle(),
                    session.getStartTime(),
                    session.getEndTime());

            viewModel.addScheduledSession(state);
        }
        viewModel.setErrorMessage("");
    }
}
