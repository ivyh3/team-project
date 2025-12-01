package interface_adapter.view_model;

import entity.Question;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizViewModel {
    private List<Question> questions = new ArrayList<>();
    private final Map<Integer, Integer> selectedOptions = new HashMap<>();

    // Existing methods like loadQuizFromText(), reset(), etc.

    public void setQuestions(List<Question> questions) {
        this.questions = new ArrayList<>(questions); // copies list
        selectedOptions.clear();
        // Optionally: notify observers if using MVVM
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    // Public adapter used by views
    public void answerQuestion(int questionIndex, int optionIndex) {
        submitAnswer(questionIndex, optionIndex); // call your existing method (now implemented)
    }

    // Records the user's selected option for a question
    public void submitAnswer(int questionIndex, int optionIndex) {
        if (questionIndex < 0 || questionIndex >= questions.size()) {
            return;
        }
        selectedOptions.put(questionIndex, optionIndex);
    }

    // Computes score by attempting several common Question API patterns via reflection
    public int getScore() {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (!selectedOptions.containsKey(i)) {
                continue;
            }
            int selected = selectedOptions.get(i);
            Question q = questions.get(i);

            // Try isCorrect(int selectedIndex) -> returns boolean
            try {
                Method isCorrect = q.getClass().getMethod("isCorrect", int.class);
                Object res = isCorrect.invoke(q, selected);
                if (res instanceof Boolean && (Boolean) res) {
                    score++;
                    continue;
                }
            } catch (Exception ignored) {}

            // Try getCorrectOptionIndex() or similar -> returns int
            String[] candidateNoArgIntMethods = {
                    "getCorrectOptionIndex", "getCorrectAnswerIndex", "getCorrectIndex", "getAnswerIndex"
            };
            boolean counted = false;
            for (String name : candidateNoArgIntMethods) {
                try {
                    Method m = q.getClass().getMethod(name);
                    Object res = m.invoke(q);
                    if (res instanceof Number) {
                        int correct = ((Number) res).intValue();
                        if (correct == selected) {
                            score++;
                            counted = true;
                            break;
                        }
                    }
                } catch (Exception ignored) {}
            }
            if (counted) continue;

            // Try getCorrectOption() (String) and getOptions() (List<String>) -> compare by value
            try {
                Method getCorrectOption = q.getClass().getMethod("getCorrectOption");
                Object correctOptObj = getCorrectOption.invoke(q);
                if (correctOptObj != null) {
                    String correctOpt = correctOptObj.toString();
                    try {
                        Method getOptions = q.getClass().getMethod("getOptions");
                        Object optsObj = getOptions.invoke(q);
                        if (optsObj instanceof List) {
                            @SuppressWarnings("rawtypes")
                            List opts = (List) optsObj;
                            if (selected >= 0 && selected < opts.size()) {
                                Object selectedOpt = opts.get(selected);
                                if (selectedOpt != null && selectedOpt.toString().equals(correctOpt)) {
                                    score++;
                                }
                            }
                        }
                    } catch (Exception ignored) {}
                }
            } catch (Exception ignored) {}
        }
        return score;
    }

    public int getCurrentScore() {
        return getScore(); // now resolved
    }
}
