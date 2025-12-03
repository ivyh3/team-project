package use_case.generate_quiz;

import java.util.Objects;

/**
 * Input data for the Generate Quiz use case.
 * Immutable and used by the interactor to generate quizzes.
 */
public class GenerateQuizInputData {
    private final String userId;
    private final String prompt;
    private final String referenceFile;

    public GenerateQuizInputData(String userId, String prompt, String referenceFile) {
        this.userId = Objects.requireNonNull(userId, "userId cannot be null");
        this.prompt = Objects.requireNonNull(prompt, "prompt cannot be null");
        this.referenceFile = Objects.requireNonNull(referenceFile, "referenceFile cannot be null");
    }

    public String getUserId() {
        return userId;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getReferenceFile() {
        return referenceFile;
    }
}
