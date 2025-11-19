package use_case.generate_quiz;

import entity.StudyQuiz;

/**
 * Output data for the Generate Quiz use case.
 */
public class GenerateQuizOutputData {
    private final StudyQuiz quiz;
    
    public GenerateQuizOutputData(StudyQuiz quiz) {
        this.quiz = quiz;
    }
    
    public StudyQuiz getQuiz() {
        return quiz;
    }
}

