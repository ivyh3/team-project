package interface_adapter.controller;

import entity.ScheduledSession;
import interface_adapter.view_model.ScheduleSessionViewModel;

public class ScheduleStudySessionController {
    private final ScheduleSessionViewModel viewModel;

    public ScheduleStudySessionController(ScheduleSessionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void addSession(ScheduledSession session) {
        viewModel.addScheduledSession(session);
        viewModel.setStatusMessage("Session scheduled successfully!");
    }
}
