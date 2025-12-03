package use_case.schedule_study_session;
import entity.ScheduledSession;
import java.util.List;

/**
 * Output boundary for the Schedule Study Session use case.
 */
public interface ScheduleStudySessionOutputBoundary {

    /**
     * Prepares the success view.
     * 
     * @param outputData the output data
     */
    void prepareSuccessView(ScheduleStudySessionOutputData outputData);

    /**
     * Prepares the failure view.
     * 
     * @param error the error message
     */
    void prepareFailView(String error);
    void prepareDeleteSuccessView(DeleteScheduledSessionOutputData outputData);
    /**
     * Sends the fetched sessions from the Interactor to the Presenter
     * for conversion and updating the ViewModel.
     * @param sessions A list of entities fetched from the database.
     */
    void loadSessions(List<ScheduledSession> sessions);
}
