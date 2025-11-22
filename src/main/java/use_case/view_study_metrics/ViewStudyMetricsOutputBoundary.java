package use_case.view_study_metrics;

/**
 * Output boundary for the View Study Metrics use case.
 */
public interface ViewStudyMetricsOutputBoundary {
    /**
     * Prepares the success view.
     * 
     * @param outputData the output data
     */
    void prepareSuccessView(ViewStudyMetricsOutputData outputData);

    /**
     * Prepares the failure view.
     * 
     * @param error the error message
     */
    void prepareFailView(String error);
}
