package repository;

import javax.swing.*;
import java.io.File;

public class FileUploader {

    public static File uploadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("User selected: " + selectedFile.getAbsolutePath());
            return selectedFile;
        }
        System.out.println("No file selected");
        return null;
    }
}
