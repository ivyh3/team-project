package frameworks_drivers.firebase;

import use_case.upload_reference_material.UploadReferenceMaterialDataAccessInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Firebase DAO for reference materials implementing the UploadReferenceMaterialDataAccessInterface.
 */
public class FirebaseFileDataAccessObject implements UploadReferenceMaterialDataAccessInterface {

    public FirebaseFileDataAccessObject() {
        // Initialize Firebase connection here if needed
    }

    @Override
    public String uploadFile(String userId, File file) throws Exception {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("userId cannot be null or empty.");
        }
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File does not exist.");
        }

        // TODO: Implement Firebase upload logic here
        // Example placeholder:
        String firebasePath = "firebase://" + userId + "/" + file.getName();
        System.out.println("Uploading file to Firebase: " + firebasePath);

        // Return a "storage path" that will be saved as metadata
        return firebasePath;
    }

    @Override
    public void saveMetadata(String userId, String storagePath, String prompt) throws Exception {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("userId cannot be null or empty.");
        }
        if (storagePath == null || storagePath.isEmpty()) {
            throw new IllegalArgumentException("storagePath cannot be null or empty.");
        }

        // TODO: Implement metadata storage in Firebase
        System.out.println("Saving metadata: " + storagePath + " with prompt: " + prompt);
    }

    @Override
    public void deleteFile(String userId, String fileName) throws Exception {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("userId cannot be null or empty.");
        }
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("fileName cannot be null or empty.");
        }

        // TODO: Implement deletion in Firebase
        System.out.println("Deleting file in Firebase: " + fileName + " for user: " + userId);
    }

    @Override
    public List<String> getAllFiles(String userId) throws Exception {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("userId cannot be null or empty.");
        }

        // TODO: Implement retrieval of all files from Firebase
        System.out.println("Fetching all files for user: " + userId);

        // Example placeholder: return a dummy list
        return new ArrayList<>();
    }
}