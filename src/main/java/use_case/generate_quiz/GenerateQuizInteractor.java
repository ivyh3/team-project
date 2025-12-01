package use_case.generate_quiz;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import entity.Question;
import interface_adapter.view_model.AnswerableQuestion;

/**
 * Interactor for the Generate Quiz use case.
 */
public final class GenerateQuizInteractor implements GenerateQuizInputBoundary {
    private static final int NUM_QUESTIONS = 10;
    private final GenerateQuizDataAccessInterface studyQuizDataAccessObject;
    private final GenerateQuizFileDataAccessInterface fileDataAccessObject;
    private GenerateQuizOutputBoundary presenter;

    public GenerateQuizInteractor(GenerateQuizOutputBoundary presenter,
            GenerateQuizDataAccessInterface studyQuizDataAccessObject,
            GenerateQuizFileDataAccessInterface fileDataAccessObject) {
        this.presenter = Objects.requireNonNull(presenter, "Output boundary cannot be null");
        this.studyQuizDataAccessObject = Objects.requireNonNull(studyQuizDataAccessObject,
                "Gemini data access cannot be null");
        this.fileDataAccessObject = Objects.requireNonNull(fileDataAccessObject,
                "File data access cannot be null");
    }

    @Override
    public void execute(GenerateQuizInputData input) {
        if (input == null || input.getUserId() == null || input.getPrompt() == null) {
            presenter.prepareFailView("Invalid input data.");
            return;
        }

        try {
            // Technically a filename. Gotta get the file from Firebase.
            String referenceFile = input.getReferenceFile();

            // I think this only supports PDFS. Great.
            byte[] pdfBytes = fileDataAccessObject.getFileContents(
                    input.getUserId(),
                    referenceFile);
            // String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

            if (pdfBytes == null) {
                presenter.prepareFailView("Reference file not found.");
                return;
            }

            // Generate quiz questions
            // List<Question> questions = studyQuizDataAccessObject.generateQuizBase64(
            // base64Pdf,
            // input.getPrompt(),
            // NUM_QUESTIONS);
            List<Question> questions = studyQuizDataAccessObject.generateQuiz(
                    pdfBytes,
                    input.getPrompt(),
                    NUM_QUESTIONS);

            if (questions.isEmpty()) {
                presenter.prepareFailView("Oops, no questions were generated!");
                return;
            }
            // Convert question entities to answerable question dataclasses for view model
            List<AnswerableQuestion> answerableQuestions = questions.stream()
                    .map(question -> new AnswerableQuestion(
                            question.getQuestion(),
                            question.getOptions(),
                            question.getCorrectIndex(),
                            question.getExplanation()))
                    .toList();

            GenerateQuizOutputData outputData = new GenerateQuizOutputData(answerableQuestions,
                    LocalDateTime.now());
            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            presenter.prepareFailView("Error generating quiz: " + e.getMessage());
        }
    }

    public void setOutputBoundary(GenerateQuizOutputBoundary output) {
        this.presenter = Objects.requireNonNull(output);
    }
}
