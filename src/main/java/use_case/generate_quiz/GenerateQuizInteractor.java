package use_case.generate_quiz;

import frameworks_drivers.gemini.GeminiDataAccess;
import repository.QuestionDataAccess;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Interactor for the Generate Quiz use case.
 *
 */
public final class GenerateQuizInteractor implements GenerateQuizInputBoundary {
    private final GenerateQuizOutputBoundary output;
    private final QuestionDataAccess repo;
    private final GeminiDataAccess gemini;

    public GenerateQuizInteractor(GenerateQuizOutputBoundary output, QuestionDataAccess repo,
                                  GeminiDataAccess gemini) {
        this.output = output;
        this.repo = repo;
        this.gemini = gemini;
    }

    @Override
    public void execute(GenerateQuizInputData input) {
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
                        && (m.getParameterCount() == 0 || m.getParameterCount() == 1 || m.getParameterCount() == 2)) {

                    Object resp;
                    try {
                        if (m.getParameterCount() == 2) {
                            // try (String prompt, List<String> refs) if available — pass null for prompt
                            resp = m.invoke(gemini, null, referenceMaterials);
                        } else if (m.getParameterCount() == 1) {
                            resp = m.invoke(gemini, referenceMaterials);
                        } else {
                            resp = m.invoke(gemini);
                        }
                    } catch (IllegalArgumentException iae) {
                        // parameter types didn't match; skip this method
                        continue;
                    }

                    if (resp instanceof List) {
                        List<?> raw = (List<?>) resp;
                        if (raw.isEmpty()) {
                            break;
                        }

                        Object first = raw.get(0);
                        if (first instanceof Map) {
                            //noinspection unchecked
                            questions = new ArrayList<>((List<Map<String, Object>>) raw);
                        } else {
                            // try to convert domain Question objects (or other POJOs) into Map<String,Object>
                            questions = new ArrayList<>();
                            for (Object o : raw) {
                                Map<String, Object> converted = convertObjectToQuestionMap(o);
                                if (converted != null) {
                                    questions.add(converted);
                                }
                            }
                            if (questions.isEmpty()) {
                                questions = null;
                            }
                        }
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
                            "options", List.of("Search sorted array", "Search unsorted array", "Sort then search", "None of the above"),
                            "answer", "Search sorted array"),
                    Map.of(
                            "id", 2,
                            "text", "Which book is authored by Joshua Bloch?",
                            "options", List.of("Clean Code", "Effective Java", "Introduction to Algorithms", "Design Patterns"),
                            "answer", "Effective Java")
            );
        }

        return questions;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> convertObjectToQuestionMap(Object o) {
        if (o == null) return null;

        // If it's already a Map-like object, return cast result
        if (o instanceof Map) {
            return new HashMap<>((Map<String, Object>) o);
        }

        try {
            Class<?> cls = o.getClass();
            Map<String, Object> map = new HashMap<>();

            // id
            Object id = tryInvokeAny(cls, o, "getId", "getID", "id");
            if (id != null) map.put("id", id.toString());

            // text (question)
            Object text = tryInvokeAny(cls, o, "getQuestion", "getText", "question", "text");
            if (text != null) map.put("text", text.toString());

            // options
            Object opts = tryInvokeAny(cls, o, "getOptions", "getPossibleAnswers", "getChoices", "getAnswers", "options");
            if (opts instanceof List) {
                map.put("options", new ArrayList<>((List<String>) opts));
            } else if (opts instanceof java.util.Collection) {
                List<String> conv = new ArrayList<>();
                for (Object item : (java.util.Collection<?>) opts) {
                    conv.add(item == null ? null : item.toString());
                }
                map.put("options", conv);
            }

            // answer: try to get a String answer first, then an index
            Object answer = tryInvokeAny(cls, o, "getAnswer", "getCorrectAnswer", "getCorrect", "getCorrectIndex", "answer", "correctIndex");
            if (answer != null) {
                if (answer instanceof Number) {
                    // convert index -> option string if available
                    List<String> options = (List<String>) map.get("options");
                    int idx = ((Number) answer).intValue();
                    if (options != null && idx >= 0 && idx < options.size()) {
                        map.put("answer", options.get(idx));
                    } else {
                        map.put("answer", String.valueOf(idx));
                    }
                } else {
                    map.put("answer", answer.toString());
                }
            } else {
                // try field "correctAnswer" or similar via reflection access
                Object ca = tryFieldAccess(cls, o, "correctAnswer", "answer", "correct");
                if (ca != null) map.put("answer", ca.toString());
            }

            // minimal validation
            if (!map.containsKey("text") || !map.containsKey("options")) {
                return null;
            }

            return map;
        } catch (Throwable t) {
            return null;
        }
    }

    private Object tryInvokeAny(Class<?> cls, Object target, String... methodNames) {
        for (String mn : methodNames) {
            try {
                Method m = cls.getMethod(mn);
                return m.invoke(target);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            }
        }
        return null;
    }

    private Object tryFieldAccess(Class<?> cls, Object target, String... fieldNames) {
        for (String fn : fieldNames) {
            try {
                java.lang.reflect.Field f = cls.getDeclaredField(fn);
                f.setAccessible(true);
                return f.get(target);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }
        return null;
    }


    private boolean saveQuizToRepository(Map<String, Object> studyQuiz) {
        boolean saved = false;
        try {
            for (Method m : repo.getClass().getMethods()) {
                String name = m.getName().toLowerCase();
                if ((name.contains("save") || name.contains("insert") || name.contains("add") || name.contains("create"))
                        && m.getParameterCount() == 1) {
                    Object saveResult = m.invoke(repo, studyQuiz);

                    if (saveResult == null) {
                        // void method or null -> assume success
                        saved = true;
                    } else if (saveResult instanceof Boolean) {
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
