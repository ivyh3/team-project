package view;

import app.AppBuilder;
import entity.Question;
import entity.StudyQuiz;
import entity.StudyQuizFactory;
import frameworks_drivers.firebase.FirebaseStudyQuizDataAccessObject;
import interface_adapter.view_model.DashboardState;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
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

        // Initialize questions after UI components exist
        questions = List.of(
                new Question("0",
                        "What is the capital of France?",
                        List.of("Berlin", "Madrid", "Paris", "Rome"),
                        2,
                        "The capital of France is Paris."),
                new Question("1",
                        "What is 2 + 2?",
                        List.of("3", "4", "5", "6"),
                        1,
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
            currentQuestion.setChosenAnswer(selectedAnswer);

            String explanation = currentQuestion.getExplanation();
            showExplanation(explanation);

            // Update score
            int correctAnswers = (int) questions.stream().filter(Question::isWasCorrect).count();
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
                int correctAnswers = (int) questions.stream().filter(Question::isWasCorrect).count();
                JOptionPane.showMessageDialog(this,
                        "Quiz completed! Final Score: " + correctAnswers + "/" + questions.size(), "Quiz Completed",
                        JOptionPane.INFORMATION_MESSAGE);
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
            questionLabel.setText((currentQuestionIndex + 1) + ". " + q.getQuestion());

            List<String> options = q.getPossibleAnswers();
            for (int i = 0; i < answerButtons.length && i < options.size(); i++) {
                answerButtons[i].setText(options.get(i));
                answerButtons[i].setSelected(false);
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
        explanationArea.setText(explanation);
    }

    public void updateScore(int correct, int total) {
        scoreLabel.setText("Score: " + correct + "/" + total);
    }
}
