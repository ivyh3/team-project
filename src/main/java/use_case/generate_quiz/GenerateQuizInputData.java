package use_case.generate_quiz;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Input data for the Generate Quiz use case.
 * Immutable and used by the interactor to generate quizzes.
 */
public class GenerateQuizInputData {
    private final String userId;
    private final String sessionId;
    private final String courseId;
    private final String prompt;
    private final List<String> referenceMaterialIds;

    public GenerateQuizInputData(String userId, String sessionId, String courseId,
                                 String prompt, List<String> referenceMaterialIds) {
        this.userId = Objects.requireNonNull(userId, "userId cannot be null");
        this.sessionId = sessionId; // optional
        this.courseId = courseId;   // optional
        this.prompt = Objects.requireNonNull(prompt, "prompt cannot be null");
        this.referenceMaterialIds = referenceMaterialIds != null ? List.copyOf(referenceMaterialIds)
                : Collections.emptyList();
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getPrompt() {
        return prompt;
    }

    public List<String> getReferenceMaterialIds() {
        return referenceMaterialIds;
    }

    /**
     * Alias for cleaner interactor usage.
     */
    public List<String> getReferenceMaterials() {
        return referenceMaterialIds;
    }
}
