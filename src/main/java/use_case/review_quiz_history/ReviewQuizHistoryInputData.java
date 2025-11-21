package use_case.review_quiz_history;

/**
 * Input data for the Review Quiz History use case.
 */
public class ReviewQuizHistoryInputData {
    private final String userId;
    private final String courseId;

    public ReviewQuizHistoryInputData(String userId, String courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCourseId() {
        return courseId;
    }
}
