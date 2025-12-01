package interface_adapter.controller;

import view.StudyQuizView;
import interface_adapter.view_model.QuizViewModel;
import use_case.generate_quiz.GenerateQuizInteractor;
import use_case.generate_quiz.GenerateQuizInputData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GenerateQuizController {
    private final StudyQuizView view;
    private final QuizViewModel viewModel;
    private GenerateQuizInteractor interactor;

    public GenerateQuizController(StudyQuizView view, QuizViewModel viewModel) {
        this.view = view;
        this.viewModel = viewModel;
    }

    public void setInteractor(GenerateQuizInteractor interactor) {
        this.interactor = interactor;
    }

    public void generateQuiz(GenerateQuizInputData inputData) {
        if (interactor != null) {
            interactor.execute(inputData);
            view.renderQuiz();
        }
    }

    public void generateQuizFromPdf(File pdfFile, String userId, String sessionId, String prompt) {
        if (interactor != null) {
            List<String> referenceMaterials = extractReferenceMaterialsFromPdf(pdfFile);
            String sourceString = (pdfFile != null) ? pdfFile.getAbsolutePath() : "";
            GenerateQuizInputData inputData = new GenerateQuizInputData(userId, sessionId, prompt, sourceString, referenceMaterials);
            interactor.execute(inputData);
            view.renderQuiz();
        }
    }

    private List<String> extractReferenceMaterialsFromPdf(File pdfFile) {
        List<String> refs = new ArrayList<>();
        if (pdfFile == null || !pdfFile.exists()) {
            return refs;
        }
        // Placeholder: replace with real PDF extraction (e.g., Apache PDFBox)
        refs.add(pdfFile.getAbsolutePath());
        return refs;
    }
}
