package use_case.view_study_metrics;

import interface_adapter.repository.StudySessionRepository;
import interface_adapter.repository.StudyQuizRepository;

/**
 * Interactor for the View Study Metrics use case.
 */
public class ViewStudyMetricsInteractor implements ViewStudyMetricsInputBoundary {
    private final StudySessionRepository sessionRepository;
    private final StudyQuizRepository quizRepository;
    private final ViewStudyMetricsOutputBoundary outputBoundary;
    
    public ViewStudyMetricsInteractor(StudySessionRepository sessionRepository,
                                     StudyQuizRepository quizRepository,
                                     ViewStudyMetricsOutputBoundary outputBoundary) {
        this.sessionRepository = sessionRepository;
        this.quizRepository = quizRepository;
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public void execute(ViewStudyMetricsInputData inputData) {
        // TODO: Implement the business logic for viewing study metrics
        // 1. Fetch all sessions and quizzes for the user (filtered by course/time)
        // 2. Calculate metrics (daily durations, average scores, etc.)
        // 3. Determine strongest/weakest subjects
        // 4. Prepare success or failure view
    }
}

