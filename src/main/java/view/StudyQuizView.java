package view;

import app.AppBuilder;
import entity.Question;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * View for displaying and taking quizzes in a study session.
 *
 * Notes:
 * - No hard-coded questions anymore. Call loadQuiz(...) with data from Gemini.
 * - Uses reflection helpers to adapt to slightly different Question APIs.
 */
public class StudyQuizView extends View {
    private final JLabel questionLabel;
    private final JRadioButton[] answerButtons;
    private final ButtonGroup answerGroup;
    private final JTextArea explanationArea;
    private final JLabel scoreLabel;
    private final JButton submitButton;
    private final JButton nextButton;

    private List<Question> questions;
    private int currentQuestionIndex;

    public StudyQuizView() {
        super("studyQuiz");

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionLabel = new JLabel("No quiz loaded");
        questionLabel.setFont(new Font(null, Font.BOLD, 24));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(questionLabel);

        // Create and configure answer buttons before loading the quiz
        answerGroup = new ButtonGroup();
        answerButtons = new JRadioButton[4];
        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i] = new JRadioButton("");
            answerButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            answerGroup.add(answerButtons[i]);
            questionPanel.add(answerButtons[i]);
            int finalI = i;
            answerButtons[i].addActionListener(e -> answerButtons[finalI].setSelected(true));
        }

        scoreLabel = new JLabel("Score: 0/0");
        JPanel header = new ViewHeader("Quiz");
        header.add(scoreLabel, BorderLayout.EAST);
        JPanel main = new JPanel();

        submitButton = createSubmitButton();
        nextButton = createNextButton();
        nextButton.setEnabled(false); // disabled until after submitting

        explanationArea = new JTextArea(5, 40);
        explanationArea.setLineWrap(true);
        explanationArea.setEditable(false);

        JPanel controls = new JPanel();
        controls.add(submitButton);
        controls.add(nextButton);

        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.add(questionPanel);
        main.add(controls);
        main.add(explanationArea);

        this.add(header, BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);

        // Start with no questions; caller must call loadQuiz(...)
        this.questions = Collections.emptyList();
        this.currentQuestionIndex = 0;
        displayCurrentQuestion();
    }

    private JButton createSubmitButton() {
        JButton submit = new JButton("Submit Answer");
        submit.addActionListener(e -> {
            int selectedAnswer = getSelectedAnswer();
            if (selectedAnswer == -1) {
                JOptionPane.showMessageDialog(this, "Please select an answer before submitting.", "No Answer Selected",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (questions == null || questions.isEmpty() || currentQuestionIndex >= questions.size()) {
                return;
            }

            Question currentQuestion = questions.get(currentQuestionIndex);
            setChosenAnswerSafely(currentQuestion, selectedAnswer);

            String explanation = getExplanationSafely(currentQuestion);
            showExplanation(explanation);

            // Update score using reflection-safe checker
            int correctAnswers = (int) questions.stream().filter(q -> isWasCorrectSafely(q)).count();
            updateScore(correctAnswers, questions.size());

            // After submitting, disable submit and enable next
            submit.setEnabled(false);
            nextButton.setEnabled(true);

            // prevent changing the selected answer after submit
            for (JRadioButton rb : answerButtons) {
                rb.setEnabled(false);
            }
        });
        return submit;
    }

    private JButton createNextButton() {
        JButton next = new JButton("Next Question");
        next.addActionListener(e -> {
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                displayCurrentQuestion();
            } else {
                int correctAnswers = (int) questions.stream().filter(q -> isWasCorrectSafely(q)).count();
                JOptionPane.showMessageDialog(this,
                        "Quiz completed! Final Score: " + correctAnswers + "/" + questions.size(), "Quiz Completed",
                        JOptionPane.INFORMATION_MESSAGE);
                // disable controls after completion
                next.setEnabled(false);
                submitButton.setEnabled(false);
            }
        });
        return next;
    }

    public void loadQuiz(List<Question> questions) {
        this.questions = (questions == null) ? Collections.emptyList() : new ArrayList<>(questions);
        this.currentQuestionIndex = 0;
        displayCurrentQuestion();
    }

    private void displayCurrentQuestion() {
        validQuestion(questions, currentQuestionIndex, questionLabel, answerButtons);
        // Reset UI state for a fresh question
        explanationArea.setText("");
        submitButton.setEnabled(true);
        nextButton.setEnabled(false);
        answerGroup.clearSelection();
        for (JRadioButton rb : answerButtons) {
            rb.setEnabled(true);
        }
    }

    static void validQuestion(List<Question> questions, int currentQuestionIndex, JLabel questionLabel,
                              JRadioButton[] answerButtons) {
        if (questions != null && currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            String questionText = getQuestionTextSafely(q);
            questionLabel.setText((currentQuestionIndex + 1) + ". " + questionText);

            List<String> options = getOptionsSafely(q);
            for (int i = 0; i < answerButtons.length; i++) {
                if (i < options.size()) {
                    answerButtons[i].setText(options.get(i));
                } else {
                    answerButtons[i].setText("");
                }
                answerButtons[i].setSelected(false);
            }
        } else {
            questionLabel.setText("No quiz loaded");
            for (JRadioButton rb : answerButtons) {
                rb.setText("");
                rb.setSelected(false);
            }
        }
    }

    public int getSelectedAnswer() {
        for (int i = 0; i < answerButtons.length; i++) {
            if (answerButtons[i].isSelected()) {
                return i;
            }
        }
        return -1;
    }

    public void showExplanation(String explanation) {
        explanationArea.setText(explanation == null ? "" : explanation);
    }

    public void updateScore(int correct, int total) {
        scoreLabel.setText("Score: " + correct + "/" + total);
    }

    // --- Reflection-safe helpers follow ---

    private static String getQuestionTextSafely(Question q) {
        if (q == null) return "";
        String[] candidates = {"getQuestion", "getQuestionText", "getText", "question", "getPrompt"};
        for (String name : candidates) {
            try {
                Method m = q.getClass().getMethod(name);
                Object r = m.invoke(q);
                if (r != null) return r.toString();
            } catch (Exception ignored) {}
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    private static List<String> getOptionsSafely(Question q) {
        if (q == null) return Collections.emptyList();
        String[] candidates = {"getPossibleAnswers", "getOptions", "getChoices", "getPossibleChoices"};
        for (String name : candidates) {
            try {
                Method m = q.getClass().getMethod(name);
                Object r = m.invoke(q);
                if (r instanceof List) {
                    return (List<String>) r;
                } else if (r instanceof String[]) {
                    String[] arr = (String[]) r;
                    List<String> out = new ArrayList<>();
                    Collections.addAll(out, arr);
                    return out;
                }
            } catch (Exception ignored) {}
        }
        return Collections.emptyList();
    }

    private static String getExplanationSafely(Question q) {
        if (q == null) return "";
        String[] candidates = {"getExplanation", "getExplanationText", "getExplanationString", "explanation"};
        for (String name : candidates) {
            try {
                Method m = q.getClass().getMethod(name);
                Object r = m.invoke(q);
                if (r != null) return r.toString();
            } catch (Exception ignored) {}
        }
        return "";
    }

    private static void setChosenAnswerSafely(Question q, int index) {
        if (q == null) return;
        List<String> options = getOptionsSafely(q);
        String stringCandidate = (index >= 0 && index < options.size()) ? options.get(index) : null;

        // Try integer setters first
        String[] intSetterCandidates = {"setChosenAnswer", "setChosenAnswerIndex", "setChosenIndex"};
        for (String name : intSetterCandidates) {
            try {
                Method m = q.getClass().getMethod(name, int.class);
                m.invoke(q, index);
                return;
            } catch (Exception ignored) {}
        }

        // Try String setters
        String[] stringSetterCandidates = {"setChosenAnswer", "setChosenAnswerString", "setChosen"};
        for (String name : stringSetterCandidates) {
            try {
                Method m = q.getClass().getMethod(name, String.class);
                m.invoke(q, stringCandidate);
                return;
            } catch (Exception ignored) {}
        }

        // Try generic single-arg setter if available
        try {
            Method[] methods = q.getClass().getMethods();
            for (Method m : methods) {
                if (m.getName().startsWith("setChosen") && m.getParameterCount() == 1) {
                    Class<?> p = m.getParameterTypes()[0];
                    if (p == int.class || p == Integer.class) {
                        m.invoke(q, index);
                        return;
                    } else if (p == String.class && stringCandidate != null) {
                        m.invoke(q, stringCandidate);
                        return;
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    private static boolean isWasCorrectSafely(Question q) {
        if (q == null) return false;

        String[] boolCandidates = {"isWasCorrect", "wasCorrect", "isCorrect", "getWasCorrect"};
        for (String name : boolCandidates) {
            try {
                Method m = q.getClass().getMethod(name);
                Object r = m.invoke(q);
                if (r instanceof Boolean) return (Boolean) r;
            } catch (Exception ignored) {}
        }

        // Fallback: compare chosen vs correct answers if possible
        try {
            Method getChosenStr = null, getCorrectStr = null;
            String[] chosenNames = {"getChosenAnswer", "getChosen", "getSelected", "chosenAnswer"};
            String[] correctNames = {"getCorrectAnswer", "getAnswer", "getCorrect", "correctAnswer"};
            for (String name : chosenNames) {
                try { getChosenStr = q.getClass().getMethod(name); break; } catch (Exception ignored) {}
            }
            for (String name : correctNames) {
                try { getCorrectStr = q.getClass().getMethod(name); break; } catch (Exception ignored) {}
            }
            Object chosen = (getChosenStr != null) ? getChosenStr.invoke(q) : null;
            Object correct = (getCorrectStr != null) ? getCorrectStr.invoke(q) : null;
            if (chosen != null && correct != null) return chosen.toString().equals(correct.toString());

            // Try comparing chosen index vs correct index
            Method getChosenIdx = null, getCorrectIdx = null;
            String[] chosenIdxNames = {"getChosenIndex", "getChosenAnswerIndex", "getSelectedIndex"};
            String[] correctIdxNames = {"getCorrectIndex", "getCorrectAnswerIndex"};
            for (String name : chosenIdxNames) {
                try { getChosenIdx = q.getClass().getMethod(name); break; } catch (Exception ignored) {}
            }
            for (String name : correctIdxNames) {
                try { getCorrectIdx = q.getClass().getMethod(name); break; } catch (Exception ignored) {}
            }
            if (getChosenIdx != null && getCorrectIdx != null) {
                Object c1 = getChosenIdx.invoke(q);
                Object c2 = getCorrectIdx.invoke(q);
                if (c1 instanceof Number && c2 instanceof Number) {
                    return ((Number) c1).intValue() == ((Number) c2).intValue();
                }
            }

        } catch (Exception ignored) {}

        return false;
    }
}
