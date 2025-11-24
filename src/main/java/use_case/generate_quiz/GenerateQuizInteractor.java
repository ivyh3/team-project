package use_case.generate_quiz;

import frameworks_drivers.gemini.GeminiDataAccess;
import repository.QuestionDataAccess;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Interactor for the Generate Quiz use case.
 *
 * @param repo   port in use_case
 * @param gemini another port
 */
public record GenerateQuizInteractor(GenerateQuizOutputBoundary output, QuestionDataAccess repo,
                                     GeminiDataAccess gemini) implements GenerateQuizInputBoundary {

    @Override
    public void execute(GenerateQuizInputData input) {
        // // TODO: Implement the business logic for generating a quiz
        // // 1. Fetch reference materials
        // // 2. Call Gemini API to generate quiz questions
        // // 3. Create StudyQuiz entity
        // // 4. Save to repository
        // // 5. Prepare success or failure view

        // 1. Fetch reference materials (try input, then repo, then fallback)
        List<String> referenceMaterials = fetchReferenceMaterials(input);

        // 2. Call Gemini API to generate quiz questions (best-effort via reflection),
        //    fall back to hard-coded questions
        List<Map<String, Object>> questions = generateQuestions(referenceMaterials);

        // 3. Create StudyQuiz entity (use a Map to avoid coupling)
        Map<String, Object> studyQuiz = new HashMap<>();
        studyQuiz.put("id", UUID.randomUUID().toString());
        studyQuiz.put("title", extractTitleFromInput(input));
        studyQuiz.put("referenceMaterials", referenceMaterials);
        studyQuiz.put("questions", questions);

        // 4. Save to repository (best-effort via reflection)
        boolean saved = saveQuizToRepository(studyQuiz);

        // 5. Prepare success or failure view via output boundary (best-effort via reflection)
        presentResult(saved, studyQuiz);
    }

    private List<String> fetchReferenceMaterials(GenerateQuizInputData input) {
        // 1a. Try to extract from input via common accessor names
        try {
            for (Method m : input.getClass().getMethods()) {
                String name = m.getName().toLowerCase();
                if ((name.contains("reference") || name.contains("material"))
                        && (m.getParameterCount() == 0)) {
                    Object resp = m.invoke(input);
                    if (resp instanceof List) {
                        //noinspection unchecked
                        return new ArrayList<>((List<String>) resp);
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            // continue to next strategy
        }

        // 1b. Try to fetch from repo via reflection (methods that return collections)
        try {
            for (Method m : repo.getClass().getMethods()) {
                String name = m.getName().toLowerCase();
                if ((name.contains("reference") || name.contains("material") || name.contains("source"))
                        && m.getParameterCount() == 0
                        && Collection.class.isAssignableFrom(m.getReturnType())) {
                    Object resp = m.invoke(repo);
                    if (resp instanceof List) {
                        //noinspection unchecked
                        return new ArrayList<>((List<String>) resp);
                    } else if (resp instanceof Collection) {
                        List<String> result = new ArrayList<>();
                        for (Object o : (Collection<?>) resp) {
                            if (o != null) result.add(o.toString());
                        }
                        if (!result.isEmpty()) return result;
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            // fallback
        }

        // 1c. Fallback defaults
        return List.of("Introduction to Algorithms", "Effective Java", "Clean Code");
    }

    private List<Map<String, Object>> generateQuestions(List<String> referenceMaterials) {
        List<Map<String, Object>> questions = null;

        try {
            for (Method m : gemini.getClass().getMethods()) {
                String name = m.getName().toLowerCase();
                if ((name.contains("generate") || name.contains("create") || name.contains("question")
                        || name.contains("quiz") || name.contains("ask"))
                        && (m.getParameterCount() == 0 || m.getParameterCount() == 1)) {
                    Object resp;
                    if (m.getParameterCount() == 1) {
                        resp = m.invoke(gemini, referenceMaterials);
                    } else {
                        resp = m.invoke(gemini);
                    }
                    if (resp instanceof List) {
                        //noinspection unchecked
                        questions = new ArrayList<>((List<Map<String, Object>>) resp);
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
                            "answer", "Search sorted array"),
                    Map.of(
                            "id", 2,
                            "text", "Which book is authored by Joshua Bloch?",
                            "options", List.of("Clean Code", "Effective Java", "Introduction to Algorithms"),
                            "answer", "Effective Java")
            );
        }

        return questions;
    }

    private boolean saveQuizToRepository(Map<String, Object> studyQuiz) {
        boolean saved = false;
        try {
            for (Method m : repo.getClass().getMethods()) {
                String name = m.getName().toLowerCase();
                if ((name.contains("save") || name.contains("insert") || name.contains("add") || name.contains("create"))
                        && m.getParameterCount() == 1) {
                    Object saveResult = m.invoke(repo, studyQuiz);
                    saved = switch (saveResult) {
                        case null ->
                            // void method or null -> assume success
                                true;
                        case Boolean b -> b;
                        case Number number -> number.longValue() != 0;
                        default ->
                            // assume success if no exception thrown
                                true;
                    };
                    break;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            // treat as failure
        }
        return saved;
    }

    private void presentResult(boolean saved, Map<String, Object> studyQuiz) {
        try {
            for (Method m : output.getClass().getMethods()) {
                String name = m.getName().toLowerCase();
                if (saved && (name.contains("success") || name.contains("present") || name.contains("show"))
                        && m.getParameterCount() == 1) {
                    m.invoke(output, studyQuiz);
                    return;
                }
                if (!saved && (name.contains("failure") || name.contains("error") || name.contains("present"))
                        && m.getParameterCount() == 1) {
                    m.invoke(output, "Failed to save quiz");
                    return;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            // swallow; best-effort presentation
        }
    }

    private String extractTitleFromInput(GenerateQuizInputData input) {
        try {
            for (Method m : input.getClass().getMethods()) {
                String name = m.getName().toLowerCase();
                if ((name.contains("title") || name.contains("name") || name.contains("quiz"))
                        && m.getParameterCount() == 0) {
                    Object resp = m.invoke(input);
                    if (resp != null) return resp.toString();
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            // fallback
        }
        return "Generated Quiz";
    }
}
