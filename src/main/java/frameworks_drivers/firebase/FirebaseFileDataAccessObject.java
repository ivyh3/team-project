package frameworks_drivers.firebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import app.Config;
import use_case.start_study_session.StartStudySessionDataAccessInterface;

// TODO: Fix this garbage.
public class FirebaseFileDataAccessObject implements StartStudySessionDataAccessInterface {

    private final String FILES_BUCKET;
    // private final Storage storage;
    private final Bucket bucket;

    public FirebaseFileDataAccessObject() {
        FILES_BUCKET = Config.getFirebaseStorageBucket();

        // TODO: Fix this garbage.
        try {
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(
                            GoogleCredentials.fromStream(new FileInputStream(Config.getFirebaseCredentialsPath())))
                    .build()
                    .getService();

            bucket = storage.get(FILES_BUCKET);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing Firebase Storage: " + e.getMessage(), e);
        }
    }

    /**
     * Uploads a file to Firebase Storage under the specified user's directory.
     * 
     * @param userId The id of the user
     * @param file   The file to upload
     * @return The path of the uploaded file
     */
    public String uploadFile(String userId, File file) {
        try {
            String fileName = file.getName();
            byte[] data = Files.readAllBytes(file.toPath());

            // Determine the MIME type of the file
            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // Fallback to a default MIME type
            }

            String filePath = String.format("users/%s/%s", userId, fileName);

            bucket.create(filePath, data, mimeType);

            return filePath;

        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a file from Firebase Storage.
     * 
     * @param userId   The id of the user
     * @param filePath The path of the file to delete
     */
    public void deleteFile(String userId, String fileName) {
        try {
            String filePath = String.format("users/%s/%s", userId, fileName);
            BlobId blobId = BlobId.of(FILES_BUCKET, filePath);
            boolean deleted = bucket.getStorage().delete(blobId);
            if (!deleted) {
                throw new RuntimeException("File not deleted, may not exist.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file: " + e.getMessage(), e);
        }
    }

    /**
     * Return whether the user's file exists within their storage bucket
     * 
     * @param userId   The id of the user
     * @param fileName The name of the file to check
     */
    public boolean fileExistsByName(String userId, String fileName) {
        String filePath = String.format("users/%s/%s", userId, fileName);
        BlobId blobId = BlobId.of(FILES_BUCKET, filePath);
        boolean exists = bucket.getStorage().get(blobId) != null;

        return exists;
    }

    /**
     * Return url paths for all files for given user.
     * 
     * @param userId The id of the user
     * @return filenames for all files belonging to the user
     */
    public List<String> getAllUserFiles(String userId) {
        List<String> fileNames = new ArrayList<>();
        String prefix = String.format("users/%s/", userId);

        try {
            bucket.list(Storage.BlobListOption.prefix(prefix)).iterateAll().forEach(blob -> {
                String filePath = blob.getName();
                String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
                fileNames.add(fileName);
            });
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user files: " + e.getMessage(), e);
        }
        return fileNames;
    }

    /**
     * Get the contents of a file as a byte array.
     * 
     * @param userId
     * @param fileName
     * @return
     */
    public byte[] getFileContents(String userId, String fileName) {
        String filePath = String.format("users/%s/%s", userId, fileName);
        try {
            BlobId blobId = BlobId.of(FILES_BUCKET, filePath);
            return bucket.getStorage().readAllBytes(blobId);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving file contents: " + e.getMessage(), e);
        }
    }

}

/**
 * ChatGPT generated test UI
 */
class Bleh {
    public static void main(String[] args) {
        FirebaseFileDataAccessObject fileDAO = new FirebaseFileDataAccessObject();

        while (true) {
            // Display a menu for the user
            String[] options = { "Upload File", "Get All Files", "Delete File", "Exit" };
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Choose an operation:",
                    "Firebase File Operations",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            try {
                switch (choice) {
                    case 0: // Upload File
                        uploadFile(fileDAO);
                        break;
                    case 1: // Get All Files
                        getAllFiles(fileDAO);
                        break;
                    case 2: // Delete File
                        deleteFile(fileDAO);
                        break;
                    case 3: // Exit
                        System.exit(0);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid choice. Try again.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage());
            }
        }
    }

    private static void uploadFile(FirebaseFileDataAccessObject fileDAO) {
        try {
            // Prompt for user ID
            String userId = JOptionPane.showInputDialog("Enter User ID:");
            if (userId == null || userId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "User ID cannot be empty.");
                return;
            }

            // Use JFileChooser to select a file
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String filePath = fileDAO.uploadFile(userId, file);
                JOptionPane.showMessageDialog(null, "File uploaded successfully! Path: " + filePath);
            } else {
                JOptionPane.showMessageDialog(null, "File upload canceled.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage(), e);
        }
    }

    private static void getAllFiles(FirebaseFileDataAccessObject fileDAO) {
        try {
            // Prompt for user ID
            String userId = JOptionPane.showInputDialog("Enter User ID:");
            if (userId == null || userId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "User ID cannot be empty.");
                return;
            }

            // Retrieve all files for the user
            List<String> filePaths = fileDAO.getAllUserFiles(userId);
            if (filePaths.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No files found for user: " + userId);
            } else {
                StringBuilder message = new StringBuilder("Files for user " + userId + ":\n");
                for (String path : filePaths) {
                    message.append(path).append("\n");
                }
                JOptionPane.showMessageDialog(null, message.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving files: " + e.getMessage(), e);
        }
    }

    private static void deleteFile(FirebaseFileDataAccessObject fileDAO) {
        try {
            // Prompt for user ID
            String userId = JOptionPane.showInputDialog("Enter User ID:");
            if (userId == null || userId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "User ID cannot be empty.");
                return;
            }

            // Prompt for file path
            String filePath = JOptionPane
                    .showInputDialog("Enter the file to delete):");
            if (filePath == null || filePath.isEmpty()) {
                JOptionPane.showMessageDialog(null, "File path cannot be empty.");
                return;
            }

            // Delete the file
            fileDAO.deleteFile(userId, filePath);
            JOptionPane.showMessageDialog(null, "File deleted successfully!");
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file: " + e.getMessage(), e);
        }
    }
}
