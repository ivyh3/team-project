package use_case.generate_quiz;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import interface_adapter.view_model.AnswerableQuestion;

/**
 * Output data for the Generate Quiz use case.
 */
public class GenerateQuizOutputData {
    private final List<AnswerableQuestion> questions;
    private final LocalDateTime startTime;

    public GenerateQuizOutputData(List<AnswerableQuestion> questions, LocalDateTime startTime) {
        this.questions = questions != null ? questions : Collections.emptyList();
        this.startTime = startTime;
    }

    public List<AnswerableQuestion> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
