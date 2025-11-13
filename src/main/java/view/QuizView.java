package view;

import entity.Question;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * View for displaying and taking quizzes.
 */
public class QuizView extends JPanel {
    private JLabel questionLabel;
    private ButtonGroup answerGroup;
    private JRadioButton[] answerButtons;
    private JButton nextButton;
    private JButton submitButton;
    private JTextArea explanationArea;
    private JLabel scoreLabel;
    
    private List<Question> questions;
    private int currentQuestionIndex;
    
    public QuizView() {
        initializeComponents();
        layoutComponents();
    }
    
    private void initializeComponents() {
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        answerGroup = new ButtonGroup();
        answerButtons = new JRadioButton[4];
        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new JRadioButton();
            answerGroup.add(answerButtons[i]);
        }
        
        nextButton = new JButton("Next Question");
        submitButton = new JButton("Submit Answer");
        explanationArea = new JTextArea(5, 40);
        explanationArea.setLineWrap(true);
        explanationArea.setEditable(false);
        scoreLabel = new JLabel("Score: 0/0");
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // TODO: Properly layout the quiz components
        // Top: Question
        // Middle: Answer options
        // Bottom: Navigation buttons, explanation, score
    }
    
    public void loadQuiz(List<Question> questions) {
        this.questions = questions;
        this.currentQuestionIndex = 0;
        displayCurrentQuestion();
    }
    
    private void displayCurrentQuestion() {
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
        scoreLabel.setText(String.format("Score: %d/%d", correct, total));
    }
}

