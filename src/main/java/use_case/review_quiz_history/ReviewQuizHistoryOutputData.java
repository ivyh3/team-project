package use_case.review_quiz_history;

import entity.StudyQuiz;
import java.util.List;

/**
 * Output data for the Review Quiz History use case.
 */
public class ReviewQuizHistoryOutputData {
    private final List<StudyQuiz> quizzes;
    
    public ReviewQuizHistoryOutputData(List<StudyQuiz> quizzes) {
        this.quizzes = quizzes;
    }
    
    public List<StudyQuiz> getQuizzes() {
        return quizzes;
    }
}

