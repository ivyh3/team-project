package view;

import entity.Question;
import interface_adapter.view_model.QuizViewModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class StudyQuizView extends JPanel {

    private final JLabel questionLabel = new JLabel();
    private final JButton nextButton = new JButton("Next");
    private final JButton quizMeButton = new JButton("Quiz Me");

    private final QuizViewModel quizViewModel;
    private final JRadioButton[] optionButtons = new JRadioButton[4];
    private final ButtonGroup optionsGroup = new ButtonGroup();

    public StudyQuizView(QuizViewModel quizViewModel) {
        this.quizViewModel = quizViewModel;
        setLayout(new BorderLayout());

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
        buttonPanel.add(quizMeButton);
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        quizMeButton.addActionListener(e -> onQuizMeClicked());
        nextButton.addActionListener(e -> goToNextQuestion());

        showLoadingState();
    }

    private void onQuizMeClicked() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File pdf = chooser.getSelectedFile();
            loadQuizFromPdfFile(pdf, "Generate 5 multiple-choice questions for this material");
        }
    }

    public void loadQuizFromPdfFile(File pdfFile, String prompt) {
        try {
            String pdfText = PdfReader.readPdf(pdfFile.getAbsolutePath());
            System.out.println("PDF text length: " + pdfText.length());
            loadQuizFromPdfText(pdfText, prompt);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Failed to load PDF: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadQuizFromPdfText(String pdfText, String prompt) {
        quizViewModel.setPrompt(prompt);
        try {
            quizViewModel.setReferenceText(pdfText);
            quizViewModel.generateQuizFromGemini(); // main call to Gemini AI
            System.out.println("Questions generated: " + quizViewModel.getQuestions().size());
            for (Question q : quizViewModel.getQuestions()) {
                System.out.println("Q: " + q.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Failed to generate quiz: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            quizViewModel.loadQuizFromText(pdfText); // fallback
        }
        updateView(); // updates UI after questions exist
    }

    public void updateView() {
        List<Question> questions = quizViewModel.getQuestions();
        int currentIndex = quizViewModel.getCurrentQuestionIndex();

        if (questions.isEmpty()) {
            questionLabel.setText("No questions generated. Try again.");
            nextButton.setEnabled(false);
            return;
        }

        if (currentIndex >= questions.size()) {
            questionLabel.setText("Quiz complete! Score: " + quizViewModel.getScore() + "/" + questions.size());
            nextButton.setEnabled(false);
            for (JRadioButton btn : optionButtons) btn.setVisible(false);
            return;
        }

        Question current = quizViewModel.getCurrentQuestion();
        questionLabel.setText("<html>" + current.getText() + "</html>");

        List<String> options = current.getOptions();
        for (int i = 0; i < optionButtons.length; i++) {
            if (i < options.size()) {
                optionButtons[i].setText(options.get(i));
                optionButtons[i].setVisible(true);
            } else {
                optionButtons[i].setVisible(false);
            }
        }
        optionsGroup.clearSelection();
        nextButton.setEnabled(true);
    }

    private void goToNextQuestion() {
        int selectedIndex = -1;
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex != -1) quizViewModel.submitAnswer(selectedIndex);
        quizViewModel.nextQuestion();
        updateView();
    }

    private void showLoadingState() {
        questionLabel.setText("Select a PDF and click 'Quiz Me' to generate questions.");
        nextButton.setEnabled(false);
        for (JRadioButton btn : optionButtons) btn.setVisible(false);
    }

    public String getViewName() {
        return "StudyQuizView";
    }
}