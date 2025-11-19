package use_case.view_study_metrics;

/**
 * Input boundary for the View Study Metrics use case.
 */
public interface ViewStudyMetricsInputBoundary {
    /**
     * Executes the view study metrics use case.
     * @param inputData the input data for viewing study metrics
     */
    void execute(ViewStudyMetricsInputData inputData);
}

