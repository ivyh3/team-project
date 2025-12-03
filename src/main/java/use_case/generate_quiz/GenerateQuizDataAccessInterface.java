package use_case.generate_quiz;

import java.util.List;

import entity.Question;

/**
 * Interface for generating a study quiz
 */
public interface GenerateQuizDataAccessInterface {
    /**
     * Generates a list of questions for a study quiz.
     * Supposed to be used when a file cannot be base64 read and using gemini file
     * api.
     *
     * @param pdfBytes the PDF file as a byte array
     * @param context      The context for the study quiz
     * @param numQuestions The number of questions to generate
     * @return list of question entities
     */
    List<Question> generateQuiz(byte[] pdfBytes, String context, int numQuestions);

    /**
     * Generates a list of questions for a study quiz.
     * 
     * @param base64Pdf    The base64 encoded PDF content
     * @param context      The context for the study quiz
     * @param numQuestions number of questions
     * @return List of question entities
     */
    List<Question> generateQuizBase64(String base64Pdf, String context, int numQuestions);
}
