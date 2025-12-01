package repository;

import javax.swing.*;
import java.io.File;

public class FileUploadHelper {

    public static File chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Uploaded file: " + selectedFile.getAbsolutePath());
            return selectedFile;
        }
        return null; // user cancelled
    }
}