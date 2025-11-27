package use_case.generate_quiz;

import frameworks_drivers.gemini.GeminiQuizDataAccess;
import repository.QuestionDataAccess;
import entity.Question;

import java.util.List;
import java.util.Objects;

/**
 * Interactor for the Generate Quiz use case.
 */
public final class GenerateQuizInteractor implements GenerateQuizInputBoundary {

    private final GenerateQuizOutputBoundary output;
    private final QuestionDataAccess questionRepo;
    private final GeminiQuizDataAccess gemini;

    public GenerateQuizInteractor(GenerateQuizOutputBoundary output,
                                  QuestionDataAccess questionRepo,
                                  GeminiQuizDataAccess gemini) {
        this.output = Objects.requireNonNull(output, "Output boundary cannot be null");
        this.questionRepo = Objects.requireNonNull(questionRepo, "Question repository cannot be null");
        this.gemini = Objects.requireNonNull(gemini, "Gemini data access cannot be null");
    }

    @Override
    public void execute(GenerateQuizInputData input) {
        if (input == null || input.getUserId() == null || input.getPrompt() == null) {
            output.prepareFailView("Invalid input data.");
            return;
        }

        try {
            // Defensive copy of reference materials
            List<String> referenceMaterials = List.copyOf(
                    input.getReferenceMaterialIds() != null ? input.getReferenceMaterialIds() : List.of()
            );

            // Generate quiz questions using Gemini
            List<Question> questions = gemini.generateQuiz(input.getPrompt(), referenceMaterials);

            if (questions == null || questions.isEmpty()) {
                output.prepareFailView("Gemini returned no questions.");
                return;
            }

            // Save questions to repository
            questionRepo.saveAll(questions);

            // Prepare output data and present success view
            GenerateQuizOutputData outputData = new GenerateQuizOutputData(
                    input.getUserId(),
                    input.getSessionId(),
                    referenceMaterials,
                    questions
            );
            output.prepareSuccessView(outputData);

        } catch (Exception e) {
            output.prepareFailView("Error generating quiz: " + e.getMessage());
        }
    }
}
