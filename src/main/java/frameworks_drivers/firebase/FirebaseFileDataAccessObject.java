package frameworks_drivers.firebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.BlobId;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Bucket;

import app.Config;
import use_case.start_study_session.StartStudySessionDataAccessInterface;
import use_case.upload_reference_material.UploadReferenceMaterialDataAccessInterface;

public class FirebaseFileDataAccessObject implements UploadReferenceMaterialDataAccessInterface, StartStudySessionDataAccessInterface {

    private final String FILES_BUCKET;
    private final Bucket bucket;

    public FirebaseFileDataAccessObject() {
        FILES_BUCKET = Config.getFirebaseStorageBucket();

        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream serviceAccount = new FileInputStream(Config.getFirebaseCredentialsPath());

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setStorageBucket(FILES_BUCKET)
                        .build();

                FirebaseApp.initializeApp(options);
            }

            bucket = StorageClient.getInstance().bucket();
        } catch (IOException e) {
            throw new RuntimeException("Error initializing Firebase Storage: " + e.getMessage(), e);
        }
    }

    public String uploadFile(String userId, File file) throws IOException {
        String fileName = file.getName();
        byte[] data = Files.readAllBytes(file.toPath());

        String mimeType = Files.probeContentType(file.toPath());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        String filePath = String.format("users/%s/%s", userId, fileName);
        bucket.create(filePath, data, mimeType);

        return filePath;
    }

    public void deleteFile(String userId, String fileName) {
        String filePath = String.format("users/%s/%s", userId, fileName);
        BlobId blobId = BlobId.of(FILES_BUCKET, filePath);
        boolean deleted = bucket.getStorage().delete(blobId);
        if (!deleted) {
            throw new RuntimeException("File not deleted, may not exist.");
        }
    }

    public List<String> getAllUserFiles(String userId) {
        List<String> fileNames = new java.util.ArrayList<>();
        String prefix = String.format("users/%s/", userId);

        bucket.list(com.google.cloud.storage.Storage.BlobListOption.prefix(prefix))
                .iterateAll()
                .forEach(blob -> {
                    String filePath = blob.getName();
                    if (!filePath.equals(prefix) && !filePath.isEmpty()) {
                        fileNames.add(filePath.substring(filePath.lastIndexOf('/') + 1));
                    }
                });

        return fileNames;
    }

    @Override
    public List<String> getAllFiles(String userId) throws Exception {
        return getAllUserFiles(userId);
    }

    @Override
    public List<String> getFilesForUser(String userId) {
        return getAllUserFiles(userId); // reuse existing method
    }

    public void saveMetadata(String userId, String storagePath, String prompt) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        Map<String, Object> data = new HashMap<>();
        data.put("fileName", storagePath.substring(storagePath.lastIndexOf("/") + 1));
        data.put("storagePath", storagePath);
        data.put("prompt", prompt);

        firestore.collection("users")
                .document(userId)
                .collection("materials")
                .document(storagePath)
                .set(data)
                .get();
    }

    public void deleteFileWithMetadata(String userId, String fileName) throws Exception {
        // Delete file from storage
        deleteFile(userId, fileName);

        // Delete Firestore metadata
        Firestore firestore = FirestoreClient.getFirestore();
        firestore.collection("users")
                .document(userId)
                .collection("materials")
                .document(String.format("users/%s/%s", userId, fileName))
                .delete()
                .get();
    }

    @Override
    public boolean fileExistsByName(String userId, String fileName) {
        String prefix = String.format("users/%s/%s", userId, fileName);
        return bucket.get(prefix) != null; // blob exists if the file exists
    }


    /*** Test UI ***/
    public static void main(String[] args) {
        FirebaseFileDataAccessObject fileDAO = new FirebaseFileDataAccessObject();

        while (true) {
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
                    case 0:
                        uploadFileUI(fileDAO);
                        break;
                    case 1:
                        getAllFilesUI(fileDAO);
                        break;
                    case 2:
                        deleteFileUI(fileDAO);
                        break;
                    case 3:
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

    private static void uploadFileUI(FirebaseFileDataAccessObject fileDAO) throws Exception {
        String userId = JOptionPane.showInputDialog("Enter User ID:");
        if (userId == null || userId.isEmpty()) return;

        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String path = fileDAO.uploadFile(userId, file);
            fileDAO.saveMetadata(userId, path, "Example prompt"); // Example prompt
            JOptionPane.showMessageDialog(null, "File uploaded successfully: " + path);
        }
    }

    private static void getAllFilesUI(FirebaseFileDataAccessObject fileDAO) {
        String userId = JOptionPane.showInputDialog("Enter User ID:");
        if (userId == null || userId.isEmpty()) return;

        List<String> files = fileDAO.getAllUserFiles(userId);
        if (files.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No files found for user " + userId);
        } else {
            JOptionPane.showMessageDialog(null, "Files:\n" + String.join("\n", files));
        }
    }

    private static void deleteFileUI(FirebaseFileDataAccessObject fileDAO) throws Exception {
        String userId = JOptionPane.showInputDialog("Enter User ID:");
        if (userId == null || userId.isEmpty()) return;

        String fileName = JOptionPane.showInputDialog("Enter file name to delete:");
        if (fileName == null || fileName.isEmpty()) return;

        fileDAO.deleteFileWithMetadata(userId, fileName);
        JOptionPane.showMessageDialog(null, "File deleted successfully!");
    }
}
