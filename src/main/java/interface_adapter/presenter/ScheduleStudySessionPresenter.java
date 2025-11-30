package interface_adapter.presenter;

import interface_adapter.view_model.ScheduleSessionViewModel;
import use_case.schedule_study_session.ScheduleStudySessionOutputBoundary;
import use_case.schedule_study_session.ScheduleStudySessionOutputData;

import java.time.format.DateTimeFormatter;

public class ScheduleStudySessionPresenter implements ScheduleStudySessionOutputBoundary {

    private final ScheduleSessionViewModel viewModel;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ScheduleStudySessionPresenter(ScheduleSessionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ScheduleStudySessionOutputData outputData) {
        var session = outputData.getSession();
        String sessionDescription = String.format(
                "%s\n%s - %s",
                session.getTitle(),
                session.getStartTime().format(formatter),
                session.getEndTime().format(formatter)
        );

        viewModel.addScheduledSession(session);
        viewModel.setStatusMessage("Session scheduled successfully!");
        viewModel.setErrorMessage("");

    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setErrorMessage(error);
        viewModel.setStatusMessage("");
    }
}
