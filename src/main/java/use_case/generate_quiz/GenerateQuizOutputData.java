package use_case.generate_quiz;

import entity.Question;
import java.util.Collections;
import java.util.List;

/**
 * Output data for the Generate Quiz use case.
 * Holds user ID, course ID, reference materials, and generated questions.
 */
public class GenerateQuizOutputData {

    private final String userId;
    private final String courseId;
    private final List<String> referenceMaterials;
    private final List<Question> questions;

    public GenerateQuizOutputData(String userId, String courseId,
                                  List<String> referenceMaterials,
                                  List<Question> questions) {
        this.userId = userId;
        this.courseId = courseId;
        this.referenceMaterials = referenceMaterials != null ? referenceMaterials : Collections.emptyList();
        this.questions = questions != null ? questions : Collections.emptyList();
    }

    public String getUserId() {
        return userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public List<String> getReferenceMaterials() {
        return Collections.unmodifiableList(referenceMaterials);
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }
}
