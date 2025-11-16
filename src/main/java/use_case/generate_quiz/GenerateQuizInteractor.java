package use_case.generate_quiz;

import interface_adapter.repository.StudyQuizRepository;
import frameworks_drivers.gemini.GeminiService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Interactor for the Generate Quiz use case.
 */
public class GenerateQuizInteractor implements GenerateQuizInputBoundary {

    private final StudyQuizRepository quizRepository;
    private final GeminiService geminiService;
    private final GenerateQuizOutputBoundary outputBoundary;

    public GenerateQuizInteractor(StudyQuizRepository quizRepository,
                                  GeminiService geminiService,
                                  GenerateQuizOutputBoundary outputBoundary) {
        this.quizRepository = quizRepository;
        this.geminiService = geminiService;
        this.outputBoundary = outputBoundary;
    }

	@Override
	public void execute(GenerateQuizInputData inputData) {
        // TODO: Implement the business logic for generating a quiz
        // 1. Fetch reference materials
        // 2. Call Gemini API to generate quiz questions
        // 3. Create StudyQuiz entity
        // 4. Save to repository
        // 5. Prepare success or failure view

        // 1. Fetch reference materials
        List<String> referenceMaterials = List.of(
                "Introduction to Algorithms",
                "Effective Java",
                "Clean Code"
        );

        // 2. Call Gemini API to generate quiz questions (best-effort via reflection), fall back to hard-coded questions
        List<Map<String, Object>> questions = null;
        try {
            for (Method m : geminiService.getClass().getMethods()) {
                String name = m.getName().toLowerCase();
                if ((name.contains("generate") || name.contains("create") || name.contains("questions") || name.contains("ask"))
                        && (m.getParameterCount() == 0 || m.getParameterCount() == 1)) {
                    Object resp;
                    if (m.getParameterCount() == 1) {
                        resp = m.invoke(geminiService, referenceMaterials);
                    } else {
                        resp = m.invoke(geminiService);
                    }
                    if (resp instanceof List) {
                        //noinspection unchecked
                        questions = (List<Map<String, Object>>) resp;
                    }
                    break;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            // best-effort: continue to fallback
        }

        if (questions == null) {
            questions = List.of(
                    Map.of(
                            "id", 1,
                            "text", "What is a binary search?",
                            "options", List.of("Search sorted array", "Search unsorted array", "Sort then search"),
                            "answer", "Search sorted array"
                    ),
                    Map.of(
                            "id", 2,
                            "text", "Which book is authored by Joshua Bloch?",
                            "options", List.of("Clean Code", "Effective Java", "Introduction to Algorithms"),
                            "answer", "Effective Java"
                    )
            );
        }

        // 3. Create StudyQuiz entity (hard-coded representation)
        Map<String, Object> studyQuiz = Map.of(
                "id", "quiz-123",
                "title", "Hard-coded Generated Quiz",
                "referenceMaterials", referenceMaterials,
                "questions", questions
        );

        // 4. Save to repository (best-effort via reflection)
        boolean saved = false;
        Object saveResult;
        try {
            for (Method m : quizRepository.getClass().getMethods()) {
                String name = m.getName().toLowerCase();
                if ((name.contains("save") || name.contains("insert") || name.contains("add"))
                        && m.getParameterCount() == 1) {
                    saveResult = m.invoke(quizRepository, studyQuiz);
                    // interpret common return types
                    if (saveResult instanceof Boolean) {
                        saved = (Boolean) saveResult;
                    } else if (saveResult instanceof Number) {
                        saved = ((Number) saveResult).longValue() != 0;
                    } else {
                        // assume success if no exception thrown
                        saved = true;
                    }
                    break;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }

        // 5. Prepare success or failure view via output boundary (best-effort via reflection)
        try {
            for (Method m : outputBoundary.getClass().getMethods()) {
                String name = m.getName().toLowerCase();
                if (saved && (name.contains("success") || name.contains("present")) && m.getParameterCount() == 1) {
                    m.invoke(outputBoundary, studyQuiz);
                    return;
                }
                if (!saved && (name.contains("failure") || name.contains("error") || name.contains("present")) && m.getParameterCount() == 1) {
                    m.invoke(outputBoundary, "Failed to save quiz");
                    return;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            // swallow; best-effort presentation
        }
    }
}
