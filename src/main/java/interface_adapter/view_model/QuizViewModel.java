package interface_adapter.view_model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Question;
import frameworks_drivers.gemini.GeminiDataAccessObject;

import java.util.ArrayList;
import java.util.List;

public class QuizViewModel {

    private final GeminiDataAccessObject dao;
    private String prompt;
    private String referenceText;
    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;

    public QuizViewModel(GeminiDataAccessObject dao) {
        this.dao = dao;
    }

    // ----------------- getters -----------------
    public List<Question> getQuestions() { return questions; }
    public int getCurrentQuestionIndex() { return currentQuestionIndex; }

    public Question getCurrentQuestion() {
        if (questions.isEmpty() || currentQuestionIndex >= questions.size()) return null;
        return questions.get(currentQuestionIndex);
    }

    public int getScore() { return score; }

    // ----------------- setters -----------------
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public void setReferenceText(String referenceText) { this.referenceText = referenceText; }

    public void reset() {
        currentQuestionIndex = 0;
        score = 0;
        for (Question q : questions) q.setAnswered(false);
    }

    public void nextQuestion() {
        if (currentQuestionIndex < questions.size()) currentQuestionIndex++;
    }

    // ----------------- generate quiz via Gemini DAO -----------------
    public void generateQuizFromGemini() throws Exception {
        if (prompt == null || referenceText == null)
            throw new IllegalStateException("Prompt or reference text is missing");

        // DAO handles all Gemini communication internally
        this.questions = dao.generateQuiz(prompt, List.of(referenceText));
        reset();
    }

    // ----------------- answer submission -----------------
    public void submitAnswer(int selectedIndex) {
        Question current = getCurrentQuestion();
        if (current == null) return;

        boolean correct = selectedIndex == current.getCorrectIndex();
        current.setAnswered(true);
        if (correct) score++;
    }

    public void submitAnswer(int questionIndex, int selectedIndex) {
        if (questionIndex < 0 || questionIndex >= questions.size()) return;

        Question question = questions.get(questionIndex);
        boolean correct = selectedIndex == question.getCorrectIndex();
        question.setAnswered(true);
        if (correct) score++;
    }

    // ----------------- load from PDF fallback -----------------
    public void loadQuizFromText(String pdfText) {
        questions.clear();
        String[] lines = pdfText.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (!lines[i].isBlank()) {
                questions.add(new Question(
                        "Q" + (i + 1),
                        lines[i],
                        List.of("A", "B", "C", "D"), // placeholder choices
                        0,
                        ""
                ));
            }
        }
        reset();
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        reset();
    }
}