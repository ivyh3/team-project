package use_case.generate_quiz;

import entity.Question;
import frameworks_drivers.gemini.GeminiDataAccessObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Objects;
import java.util.Arrays;

/**
 * Interactor for the Generate Quiz use case.
 */
public final class GenerateQuizInteractor implements GenerateQuizInputBoundary {

    private final GenerateQuizOutputBoundary output;
    private final GeminiDataAccessObject geminiDao;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
            List<String> referenceMaterials = List.copyOf(
                    input.getReferenceMaterialIds() != null ? input.getReferenceMaterialIds() : List.of()
            );

            // Call Gemini DAO method which returns a raw JSON String
            String rawQuestions = geminiDao.generateQuestions(input.getPrompt(), referenceMaterials);

            // Parse the raw JSON String into List<Question>
            List<Question> questions = Arrays.asList(objectMapper.readValue(rawQuestions, Question[].class));

            if (questions == null || questions.isEmpty()) {
                output.prepareFailView("Gemini returned no questions.");
                return;
            }

            GenerateQuizOutputData outputData = new GenerateQuizOutputData(
                    input.getUserId(),
                    input.getSessionId(),
                    referenceMaterials,
                    questions
            );
            output.prepareSuccessView(outputData);

        } catch (JsonProcessingException e) {
            output.prepareFailView("Failed to parse questions: " + e.getMessage());
        } catch (Exception e) {
            output.prepareFailView("Error generating quiz: " + e.getMessage());
        }
    }
}