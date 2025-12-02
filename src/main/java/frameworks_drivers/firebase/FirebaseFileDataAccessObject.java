package frameworks_drivers.firebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import app.Config;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import use_case.start_study_session.StartStudySessionDataAccessInterface;

public class FirebaseFileDataAccessObject implements StartStudySessionDataAccessInterface {
    private final String filesBucket;
    private final Bucket bucket;

    public FirebaseFileDataAccessObject() {
        filesBucket = Config.getFirebaseStorageBucket();
        try {
            final Storage storage = StorageOptions.newBuilder()
                    .setCredentials(
                            GoogleCredentials.fromStream(new FileInputStream(Config.getFirebaseCredentialsPath())))
                    .build()
                    .getService();

            bucket = storage.get(filesBucket);
        }
        catch (IOException err) {
            throw new RuntimeException("Error initializing Firebase Storage: " + err.getMessage(), err);
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
            final String fileName = file.getName();
            final byte[] data = Files.readAllBytes(file.toPath());

            // Determine the MIME type of the file
            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) {
                // Fallback to a default MIME type
                mimeType = "application/octet-stream";
            }

            final String filePath = getFilePath(userId, fileName);

            bucket.create(filePath, data, mimeType);

            return filePath;

        }
        catch (IOException err) {
            throw new RuntimeException("Error uploading file: " + err.getMessage(), err);
        }
    }

    /**
     * Deletes a file from Firebase Storage.
     * 
     * @param userId   The id of the user
     * @param fileName The name of the file to delete
     */
    public void deleteFile(String userId, String fileName) {
        try {
            final String filePath = getFilePath(userId, fileName);
            final BlobId blobId = BlobId.of(filesBucket, filePath);
            final boolean deleted = bucket.getStorage().delete(blobId);
            if (!deleted) {
                throw new RuntimeException("File not deleted, may not exist.");
            }
        }
        catch (Exception err) {
            throw new RuntimeException("Error deleting file: " + err.getMessage(), err);
        }
    }

    /**
     * Return whether the user's file exists within their storage bucket.
     * 
     * @param userId   The id of the user
     * @param fileName The name of the file to check
     */
    public boolean fileExistsByName(String userId, String fileName) {
        final String filePath = getFilePath(userId, fileName);
        final BlobId blobId = BlobId.of(filesBucket, filePath);

        return bucket.getStorage().get(blobId) != null;
    }

    /**
     * Return url paths for all files for given user.
     * 
     * @param userId The id of the user
     * @return filenames for all files belonging to the user
     */
    public List<String> getAllUserFiles(String userId) {
        final List<String> fileNames = new ArrayList<>();
        final String prefix = String.format("users/%s/", userId);

        try {
            bucket.list(Storage.BlobListOption.prefix(prefix)).iterateAll().forEach(blob -> {
                final String filePath = blob.getName();

                // Skip the prefix itself or empty file paths
                if (filePath.equals(prefix) || filePath.isEmpty()) {
                    return;
                }

                final String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
                fileNames.add(fileName);
            });
        }
        catch (Exception err) {
            throw new RuntimeException("Error retrieving user files: " + err.getMessage(), err);
        }
        return fileNames;
    }

    /**
     * Get the contents of a file as a byte array.
     * 
     * @param userId The userId
     * @param fileName the filaname 
     * @return the byte[] of the content
     */
    public byte[] getFileContents(String userId, String fileName) {
        final String filePath = getFilePath(userId, fileName);
        try {
            final BlobId blobId = BlobId.of(filesBucket, filePath);
            return bucket.getStorage().readAllBytes(blobId);
        }
        catch (Exception e) {
            throw new RuntimeException("Error retrieving file contents: " + e.getMessage(), e);
        }
    }

    /**
     * Given the userID and the filename, return the file path for this file in firebase storage.
     * @param userId The user Id
     * @param fileName The file name
     * @return The path of the file
     */
    private String getFilePath(String userId, String fileName) {
        return String.format("users/%s/%s", userId, fileName);
    }

}
