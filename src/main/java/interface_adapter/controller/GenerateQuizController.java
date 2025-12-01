package interface_adapter.controller;

import javax.swing.JOptionPane;

import use_case.generate_quiz.GenerateQuizInputBoundary;
import use_case.generate_quiz.GenerateQuizInputData;

/**
 * Controller for generating a study quiz.
 * Receives user input and delegates quiz generation to the interactor.
 */
public class GenerateQuizController {

    private final GenerateQuizInputBoundary interactor;

    public GenerateQuizController(GenerateQuizInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String userId, String fileName, String context) {
        if (userId == null || fileName == null || context == null) {
            JOptionPane.showMessageDialog(null, "Invalid input for quiz generation.", "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        GenerateQuizInputData inputData = new GenerateQuizInputData(userId, context, fileName);
        interactor.execute(inputData);
    }
}
