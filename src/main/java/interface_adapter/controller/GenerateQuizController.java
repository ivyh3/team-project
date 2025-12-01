package interface_adapter.controller;

import view.StudyQuizView;

import javax.swing.*;
import java.io.File;

public class GenerateQuizController {

    private final StudyQuizView studyQuizView;

    public GenerateQuizController(StudyQuizView studyQuizView) {
        this.studyQuizView = studyQuizView;
    }

    public void loadPdfAndGenerateQuiz(File pdfFile, String prompt) {
        if (pdfFile == null || !pdfFile.exists()) {
            JOptionPane.showMessageDialog(null, "PDF file is invalid or does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        studyQuizView.loadQuizFromPdfFile(pdfFile, prompt);
    }
}