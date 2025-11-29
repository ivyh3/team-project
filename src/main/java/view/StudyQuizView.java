package view;

import interface_adapter.view_model.QuizState;
import interface_adapter.view_model.QuizViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Fully passive Study Quiz View.
 * Observes QuizViewModel and updates the UI automatically.
 * Does NOT hold business state or mutate ViewModel directly.
 */
public class StudyQuizView extends StatefulView<QuizState> implements PropertyChangeListener {

    private final QuizViewModel quizViewModel; // <-- add this field

    private final JLabel questionLabel;
    private final JPanel optionsPanel;
    private final JTextArea explanationArea;
    private final JLabel scoreLabel;
    private final JButton submitButton;
    private final JButton nextButton;

    private ButtonGroup optionGroup;

    public StudyQuizView(QuizViewModel quizViewModel) {
        super("quiz", quizViewModel);
        this.quizViewModel = quizViewModel; // <-- assign constructor param to field
        this.quizViewModel.addPropertyChangeListener(this);

        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        scoreLabel = new JLabel(this.quizViewModel.getScoreDisplay());
        header.add(scoreLabel, BorderLayout.EAST);

        // --- Question ---
        questionLabel = new JLabel(this.quizViewModel.getCurrentQuestion());
        questionLabel.setFont(new Font(null, Font.BOLD, 24));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Options Panel ---
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionGroup = new ButtonGroup();
        renderOptions(this.quizViewModel.getCurrentOptions());

        // --- Explanation Area ---
        explanationArea = new JTextArea(5, 40);
        explanationArea.setEditable(false);
        explanationArea.setLineWrap(true);
        explanationArea.setText(this.quizViewModel.getExplanation());

        // --- Buttons ---
        submitButton = new JButton("Submit");
        nextButton = new JButton("Next");
        nextButton.setEnabled(false);

        JPanel controls = new JPanel();
        controls.add(submitButton);
        controls.add(nextButton);

        // --- Main Panel Layout ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(questionLabel);
        mainPanel.add(optionsPanel);
        mainPanel.add(controls);
        mainPanel.add(explanationArea);

        this.add(header, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);

        // --- Observe ViewModel ---
        this.quizViewModel.addPropertyChangeListener(this);
    }

    // --- Bind user actions to controller callbacks ---
    public void bindSubmitAction(Runnable onSubmit) {
        submitButton.addActionListener(e -> onSubmit.run());
    }

    public void bindNextAction(Runnable onNext) {
        nextButton.addActionListener(e -> onNext.run());
    }

    // --- Render options from ViewModel ---
    private void renderOptions(List<String> options) {
        optionsPanel.removeAll();
        optionGroup = new ButtonGroup();

        if (options != null) {
            for (int i = 0; i < options.size(); i++) {
                int idx = i;
                JRadioButton btn = new JRadioButton(options.get(i));
                btn.addActionListener(e -> quizViewModel.setSelectedAnswer(idx));
                optionGroup.add(btn);
                optionsPanel.add(btn);
            }
        }

        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    // --- PropertyChangeListener to update UI automatically ---
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> {
            switch (evt.getPropertyName()) {
                case "currentQuestion":
                    questionLabel.setText((String) evt.getNewValue());
                    break;
                case "currentOptions":
                    renderOptions((List<String>) evt.getNewValue());
                    break;
                case "explanation":
                    explanationArea.setText((String) evt.getNewValue());
                    break;
                case "scoreDisplay":
                    scoreLabel.setText((String) evt.getNewValue());
                    break;
                case "submitEnabled":
                    submitButton.setEnabled((Boolean) evt.getNewValue());
                    break;
                case "nextEnabled":
                    nextButton.setEnabled((Boolean) evt.getNewValue());
                    break;
                case "quizComplete":
                    boolean complete = (Boolean) evt.getNewValue();
                    submitButton.setEnabled(!complete);
                    nextButton.setEnabled(!complete);
                    break;
            }
        });
    }
}
