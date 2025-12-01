package view;

import interface_adapter.view_model.QuizViewModel;
import interface_adapter.view_model.StudySessionEndState;
import interface_adapter.view_model.StudySessionEndViewModel;
import entity.Question;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.List;

public class StudySessionEndView extends StatefulView<StudySessionEndState> {

    private final JLabel resultLabel = new JLabel();
    private final JPanel quizPanel = new JPanel();
    private QuizViewModel quizViewModel;

    public void setQuizDependencies(QuizViewModel quizVM) {
        this.quizViewModel = quizVM;
    }

    public StudySessionEndView(StudySessionEndViewModel viewModel) {
        super("studySessionEnd", viewModel);

        setLayout(new BorderLayout());

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Study Session End");
        titleLabel.setFont(new Font(null, Font.BOLD, 52));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        resultLabel.setFont(new Font(null, Font.BOLD, 28));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS));

        main.add(Box.createVerticalGlue());
        main.add(titleLabel);
        main.add(resultLabel);
        main.add(Box.createVerticalStrut(20));
        main.add(quizPanel);
        main.add(Box.createVerticalGlue());

        add(main, BorderLayout.CENTER);
    }

    private final JLabel scoreLabel = new JLabel();

    private void renderQuiz() {
        quizPanel.removeAll();
        quizPanel.add(scoreLabel);

        if (quizViewModel == null) {
            scoreLabel.setText("");
            quizPanel.add(new JLabel("Quiz view model is not set."));
            quizPanel.revalidate();
            quizPanel.repaint();
            return;
        }

        List<Question> questions = quizViewModel.getQuestions();
        if (questions == null || questions.isEmpty()) {
            scoreLabel.setText("");
            quizPanel.add(new JLabel("No questions generated."));
            quizPanel.revalidate();
            quizPanel.repaint();
            return;
        }

        Question question = quizViewModel.getCurrentQuestion();
        if (question == null) {
            scoreLabel.setText("Quiz complete! Score: " + quizViewModel.getScore() + "/" + questions.size());
            quizPanel.revalidate();
            quizPanel.repaint();
            return;
        }

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

        JLabel qLabel = new JLabel("<html>" + question.getText() + "</html>");
        questionPanel.add(qLabel);

        ButtonGroup group = new ButtonGroup();
        java.util.List<String> options = question.getOptions();
        for (int i = 0; i < options.size(); i++) {
            final int optionIndex = i;
            JRadioButton rb = new JRadioButton(options.get(i));
            rb.addActionListener(e -> {
                quizViewModel.submitAnswer(optionIndex);
                quizViewModel.nextQuestion();
                SwingUtilities.invokeLater(this::renderQuiz);
            });
            group.add(rb);
            questionPanel.add(rb);
        }

        quizPanel.add(questionPanel);
        quizPanel.add(Box.createVerticalStrut(15));

        scoreLabel.setText("Score: " + quizViewModel.getScore() + "/" + questions.size());

        quizPanel.revalidate();
        quizPanel.repaint();
    }

    private String formatDurationSafe(java.time.Duration duration) {
        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        seconds %= 60;
        long hours = minutes / 60;
        minutes %= 60;
        if (hours > 0) return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        else return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        StudySessionEndState state = (StudySessionEndState) evt.getNewValue();
        resultLabel.setText("You studied for " + formatDurationSafe(state.getDuration()));

        if (quizViewModel != null) {
            renderQuiz();
        }
    }
}