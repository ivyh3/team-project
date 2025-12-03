package view;

import interface_adapter.controller.GenerateQuizController;
import interface_adapter.view_model.QuizViewModel;
import entity.Question;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * View for generating and displaying quizzes.
 */
public class StudyQuizView extends JPanel {

    private QuizViewModel quizViewModel = new QuizViewModel();
    private final JButton generateQuizButton;
    private final JPanel quizPanel;
    private GenerateQuizController controller;

    public StudyQuizView() {
        this.quizViewModel = quizViewModel;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Study Quiz");
        titleLabel.setFont(new Font(null, Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        generateQuizButton = new JButton("Generate Quiz");
        generateQuizButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateQuizButton.addActionListener(e -> {
            if (controller != null) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select PDF Reference File");
                int result = fileChooser.showOpenDialog(this);
                if (result != JFileChooser.APPROVE_OPTION) return;

                File pdfFile = fileChooser.getSelectedFile();

                // Prompt for required values (on EDT)
                String userId = JOptionPane.showInputDialog(this, "Enter user ID:", "user123");
                if (userId == null) return;
                String sessionId = JOptionPane.showInputDialog(this, "Enter session ID:", "session123");
                if (sessionId == null) return;
                String prompt = JOptionPane.showInputDialog(this, "Enter prompt (optional):", "");

                // create final copies for use inside the background lambda
                final File finalPdfFile = pdfFile;
                final String finalUserId = userId;
                final String finalSessionId = sessionId;
                final String finalPrompt = (prompt == null) ? "" : prompt;

                // Run quiz generation off the Event Dispatch Thread to prevent UI freezing
                new Thread(() -> {
                    controller.generateQuizFromPdf(finalPdfFile, finalUserId, finalSessionId, finalPrompt);
                    SwingUtilities.invokeLater(this::renderQuiz);
                }).start();
            }
        });

        quizPanel = new JPanel();
        quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS));

        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createVerticalStrut(15));
        add(generateQuizButton);
        add(Box.createVerticalStrut(20));
        add(quizPanel);
        add(Box.createVerticalGlue());
    }

    public void setController(GenerateQuizController controller) {
        this.controller = controller;
    }

    public void renderQuiz() {
        quizPanel.removeAll();

        if (quizViewModel.getQuestions().isEmpty()) {
            quizPanel.add(new JLabel("No questions generated."));
        } else {
            for (int qIndex = 0; qIndex < quizViewModel.getQuestions().size(); qIndex++) {
                final int finalQIndex = qIndex;
                Question q = quizViewModel.getQuestions().get(qIndex);

                JPanel qPanel = new JPanel();
                qPanel.setLayout(new BoxLayout(qPanel, BoxLayout.Y_AXIS));

                JLabel qLabel = new JLabel("<html>" + q.getText() + "</html>");
                qPanel.add(qLabel);

                ButtonGroup group = new ButtonGroup();
                for (int i = 0; i < q.getOptions().size(); i++) {
                    final int optionIndex = i;
                    JRadioButton optionButton = new JRadioButton(q.getOptions().get(i));
                    optionButton.addActionListener(ev -> {
                        quizViewModel.answerQuestion(finalQIndex, optionIndex);
                        SwingUtilities.invokeLater(this::renderQuiz);
                    });
                    group.add(optionButton);
                    qPanel.add(optionButton);
                }

                quizPanel.add(qPanel);
                quizPanel.add(Box.createVerticalStrut(10));
            }

            JLabel scoreLabel = new JLabel("Score: " + quizViewModel.getCurrentScore() + "/" + quizViewModel.getQuestions().size());
            scoreLabel.setFont(new Font(null, Font.BOLD, 16));
            quizPanel.add(scoreLabel);
        }

        quizPanel.revalidate();
        quizPanel.repaint();
    }

    public String getViewName() {return "studyQuiz";}
}
