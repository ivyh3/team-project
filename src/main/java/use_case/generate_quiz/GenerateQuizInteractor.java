package use_case.generate_quiz;

import entity.Question;
import frameworks_drivers.gemini.GeminiDataAccessObject;

import java.util.List;
import java.util.Objects;

/**
 * Interactor for the Generate Quiz use case.
 */
public final class GenerateQuizInteractor implements GenerateQuizInputBoundary {

    private final GenerateQuizOutputBoundary output;
    private final GeminiDataAccessObject geminiDao;

    public GenerateQuizInteractor(GenerateQuizOutputBoundary output,
                                  GeminiDataAccessObject geminiDao) {
        this.output = Objects.requireNonNull(output, "Output boundary cannot be null");
        this.geminiDao = Objects.requireNonNull(geminiDao, "Gemini DAO cannot be null");
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

            // Generate quiz questions using Gemini DAO
            List<Question> questions = geminiDao.generateQuiz(input.getPrompt(), referenceMaterials);

            if (questions == null || questions.isEmpty()) {
                output.prepareFailView("Gemini returned no questions.");
                return;
            }

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