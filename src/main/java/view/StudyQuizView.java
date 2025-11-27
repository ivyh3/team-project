package view;

import app.AppBuilder;
import entity.Question;
import entity.StudyQuiz;
import entity.StudyQuizFactory;
import frameworks_drivers.firebase.FirebaseStudyQuizDataAccessObject;
import interface_adapter.view_model.DashboardState;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * View for displaying and taking quizzes in a study session.
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
    private boolean[] wasCorrect;

    public StudyQuizView() {
        super("studyQuiz");

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionLabel = new JLabel("Question:");
        questionLabel.setFont(new Font(null, Font.BOLD, 24));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(questionLabel);

        String[] options = { "Berlin", "Madrid", "Paris", "Rome" };

        // Create and configure answer buttons before loading the quiz
        answerGroup = new ButtonGroup();
        answerButtons = new JRadioButton[4];
        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i] = new JRadioButton(options[i]);
            answerButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            answerGroup.add(answerButtons[i]);
            questionPanel.add(answerButtons[i]);
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

        // Initialize questions after UI components exist
        questions = List.of(
                // NOTE: correctIndex is 0-based (0 -> Berlin, 1 -> Madrid, 2 -> Paris, 3 -> Rome)
                new Question("0",
                        "What is the capital of France?",
                        List.of("Berlin", "Madrid", "Paris", "Rome"),
                        2, // <- index of "Paris"
                        "The capital of France is Paris."),
                new Question("1",
                        "What is 2 + 2?",
                        List.of("3", "4", "5", "6"),
                        1, // <- index of "4"
                        "2 + 2 equals 4."));
        this.loadQuiz(questions);
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

            Question currentQuestion = questions.get(currentQuestionIndex);

            List<String> options = currentQuestion.getOptions();
            String chosenText = (selectedAnswer >= 0 && selectedAnswer < options.size()) ? options.get(selectedAnswer) : null;
            String correctText = resolveCorrectAnswer(currentQuestion);

            boolean correct = correctText != null && correctText.equals(chosenText);
            wasCorrect[currentQuestionIndex] = correct;

            String explanation = resolveExplanation(currentQuestion);
            showExplanation(explanation == null ? "" : explanation);

            // Update score using helper
            int correctAnswers = countCorrectAnswers();
            updateScore(correctAnswers, questions.size());

            // After submitting, disable submit and enable next
            submit.setEnabled(false);
            nextButton.setEnabled(true);

            // prevent changing the selected answer after submit
            setAnswerButtonsEnabled(false);
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
                int correctAnswers = countCorrectAnswers();
                JOptionPane.showMessageDialog(this,
                        "Quiz completed! Final Score: " + correctAnswers + "/" + questions.size()
                );
                // disable controls after completion
                next.setEnabled(false);
                submitButton.setEnabled(false);

                // TODO: This is temporary to enable full flow.
                AppBuilder.viewManagerModel.setView("dashboard");
                FirebaseStudyQuizDataAccessObject quizDAO = new FirebaseStudyQuizDataAccessObject(
                        new StudyQuizFactory());

                float score = (float) correctAnswers / questions.size();
                StudyQuiz quiz = new StudyQuizFactory().create(score,
                        LocalDateTime.now(), LocalDateTime.now());
                quizDAO.addStudyQuiz(DashboardState.userId,
                        quiz);
            }
        });
        return next;
    }

    public void loadQuiz(List<Question> questions) {
        this.questions = questions;
        this.currentQuestionIndex = 0;
        this.wasCorrect = new boolean[questions.size()];
        displayCurrentQuestion();
    }

    private void displayCurrentQuestion() {
        validQuestion(questions, currentQuestionIndex, questionLabel);
        // Reset UI state for a fresh question
        explanationArea.setText("");
        submitButton.setEnabled(true);
        nextButton.setEnabled(false);
        clearAnswerButtonSelection();
        setAnswerButtonsEnabled(true);
    }

    private void validQuestion(List<Question> questions, int currentQuestionIndex, JLabel questionLabel) {
        if (questions != null && currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            // Use the presenter's API: getText(), getOptions()
            questionLabel.setText((currentQuestionIndex + 1) + ". " + q.getText());

            List<String> options = q.getOptions();
            updateAnswerButtonsText(options);
            clearAnswerButtonSelection();
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
        explanationArea.setText(explanation);
    }

    public void updateScore(int correct, int total) {
        scoreLabel.setText("Score: " + correct + "/" + total);
    }

    // --- Reflection helpers to tolerate different Question APIs ---

    private String resolveCorrectAnswer(Question q) {
        Object resp = tryInvokeAny(q, "getAnswer", "getCorrectAnswer", "getCorrect", "correctAnswer", "getCorrectIndex");
        String result = answerFromObject(resp, q);
        if (result != null) return result;

        Object fieldVal = tryFieldAccess(q, "correctAnswer", "answer", "correct");
        return answerFromObject(fieldVal, q);
    }

    private String resolveExplanation(Question q) {
        Object resp = tryInvokeAny(q, "getExplanation", "getExplanationText", "getDetails", "explanation");
        if (resp != null) return resp.toString();
        Object fieldVal = tryFieldAccess(q, "explanation", "details");
        return fieldVal == null ? null : fieldVal.toString();
    }

    @SuppressWarnings("unchecked")
    private List<String> safeGetOptions(Question q) {
        Object resp = tryInvokeAny(q, "getOptions", "getPossibleAnswers", "getChoices", "getAnswers");
        if (resp instanceof List) return (List<String>) resp;
        if (resp instanceof Collection) {
            return List.copyOf((Collection<String>) resp);
        }
        return null;
    }

    private Object tryInvokeAny(Object target, String... names) {
        if (target == null) return null;
        Class<?> cls = target.getClass();
        for (String n : names) {
            try {
                Method m = cls.getMethod(n);
                return m.invoke(target);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            }
        }
        return null;
    }

    private Object tryFieldAccess(Object target, String... names) {
        if (target == null) return null;
        Class<?> cls = target.getClass();
        for (String n : names) {
            try {
                Field f = cls.getDeclaredField(n);
                f.setAccessible(true);
                return f.get(target);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }
        return null;
    }

    private void setAnswerButtonsEnabled(boolean enabled) {
        for (JRadioButton rb : answerButtons) {
            rb.setEnabled(enabled);
        }
    }

    private void clearAnswerButtonSelection() {
        answerGroup.clearSelection();
    }

    private void updateAnswerButtonsText(List<String> options) {
        for (int i = 0; i < answerButtons.length; i++) {
            if (i < options.size()) {
                answerButtons[i].setText(options.get(i));
                answerButtons[i].setVisible(true);
            } else {
                answerButtons[i].setText("");
                answerButtons[i].setVisible(false);
            }
        }
    }

    private int countCorrectAnswers() {
        int correct = 0;
        for (boolean b : wasCorrect) if (b) correct++;
        return correct;
    }

    private String answerFromObject(Object value, Question q) {
        if (value == null) return null;
        if (value instanceof Number) {
            int idx = ((Number) value).intValue();
            List<String> opts = safeGetOptions(q);
            if (opts != null && idx >= 0 && idx < opts.size()) return opts.get(idx);
            return String.valueOf(idx);
        }
        return value.toString();
    }
}
