package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import app.AppBuilder;
import interface_adapter.view_model.AnswerableQuestion;
import interface_adapter.view_model.DashboardViewModel;
import interface_adapter.view_model.QuizState;
import interface_adapter.view_model.QuizViewModel;

public class StudyQuizView extends StatefulView<QuizState> {

    private final JLabel questionLabel = new JLabel();
    private final JButton nextButton = new JButton("Next");

    private final JRadioButton[] optionButtons = new JRadioButton[4];
    private final ButtonGroup optionsGroup = new ButtonGroup();

    public StudyQuizView(QuizViewModel quizViewModel, DashboardViewModel dashboardViewModel) {
        super("studyQuiz", quizViewModel);

        // Question label
        questionLabel.setFont(new Font(null, Font.PLAIN, 20));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(questionLabel, BorderLayout.NORTH);

        // Options panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new JRadioButton();
            optionsGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        add(optionsPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        nextButton.addActionListener(e -> goToNextQuestion());

    }

    private void goToNextQuestion() {
        // Already answered, nextquestion.
        if (viewModel.getState().getCurrentQuestion().isAnswered()) {
            if (viewModel.getState().isQuizComplete()) {
                JOptionPane.showMessageDialog(this,
                        String.format("You got %d out of %d correct", viewModel.getState().getNumCorrect(),
                                viewModel.getState().getNumCorrect()),
                        "Done!",
                        JOptionPane.INFORMATION_MESSAGE);
                // TODO: Temporary everything is temporary here bro
                AppBuilder.viewManagerModel.setView("dashboard");

            }
            viewModel.getState().nextQuestion();
        }
        // Did not answer yet, submit answer and show the explanation
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                viewModel.getState().submitAnswer(i);
                break;
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        AnswerableQuestion currentQuestion = viewModel.getState().getCurrentQuestion();
        questionLabel.setText(currentQuestion.getQuestionText());
        List<String> options = currentQuestion.getChoices();
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options.get(i));
            optionButtons[i].setSelected(false);
        }
        if (currentQuestion.isAnswered()) {
            String message;
            if (currentQuestion.isAnsweredCorrectly()) {
                message = "Correct!\n\n" + currentQuestion.getExplanation();
            } else {
                message = String.format("Incorrect! The correct answer was: %s\n\n%s",
                        currentQuestion.getChoices().get(currentQuestion.getCorrectIndex()),
                        currentQuestion.getExplanation());
            }
            JOptionPane.showMessageDialog(this, message, "Answer Explanation", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
