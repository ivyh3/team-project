package frameworks_drivers.firebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;

import app.Config;
import use_case.start_study_session.StartStudySessionDataAccessInterface;
import use_case.upload_reference_material.UploadReferenceMaterialDataAccessInterface;

/**
 * Firebase implementation for file storage and metadata management.
 */
public class FirebaseFileDataAccessObject implements UploadReferenceMaterialDataAccessInterface,
        StartStudySessionDataAccessInterface {

    private final String FILES_BUCKET;
    private final Bucket bucket;

    public FirebaseFileDataAccessObject() {
        this.FILES_BUCKET = Config.getFirebaseStorageBucket();
        try {
            if (!Config.isFirebaseInitialized()) {
                Config.initializeFirebase();
            }
            this.bucket = StorageClient.getInstance().bucket();
        } catch (IOException e) {
            throw new RuntimeException("Error initializing Firebase: " + e.getMessage(), e);
        }
    }

    private String getUserFilePath(String userId, String fileName) {
        return String.format("users/%s/%s", userId, fileName);
    }

    @Override
    public String uploadFile(String userId, File file) throws IOException {
        String timestampedFileName = System.currentTimeMillis() + "_" + file.getName();
        String path = getUserFilePath(userId, timestampedFileName);
        String mimeType = java.nio.file.Files.probeContentType(file.toPath());
        if (mimeType == null) mimeType = "application/octet-stream";

        // Stream upload to avoid large file memory issues
        try (FileInputStream fis = new FileInputStream(file)) {
            bucket.create(path, fis, mimeType);
        }

        return path;
    }

    @Override
    public void saveMetadata(String userId, String storagePath, String prompt) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        Map<String, Object> data = new HashMap<>();
        String fileName = storagePath.substring(storagePath.lastIndexOf("/") + 1);

        data.put("fileName", fileName);
        data.put("storagePath", storagePath);
        data.put("prompt", prompt);

        // Use full storage path as document ID to avoid collisions
        firestore.collection("users")
                .document(userId)
                .collection("materials")
                .document(storagePath.replace("/", "_"))
                .set(data)
                .get();
    }

    @Override
    public void deleteFile(String userId, String fileName) throws Exception {
        String path = getUserFilePath(userId, fileName);
        BlobId blobId = BlobId.of(FILES_BUCKET, path);
        boolean deleted = bucket.getStorage().delete(blobId);
        if (!deleted) {
            throw new RuntimeException("File not found or could not be deleted: " + path);
        }
    }

    public void deleteFileWithMetadata(String userId, String fileName) throws Exception {
        deleteFile(userId, fileName);

        Firestore firestore = FirestoreClient.getFirestore();
        firestore.collection("users")
                .document(userId)
                .collection("materials")
                .document(getUserFilePath(userId, fileName).replace("/", "_"))
                .delete()
                .get();
    }

    @Override
    public boolean fileExistsByName(String userId, String fileName) {
        Blob blob = bucket.get(getUserFilePath(userId, fileName));
        return blob != null;
    }

    public String getFileContent(String userId, String fileName) throws IOException {
        Blob blob = bucket.get(getUserFilePath(userId, fileName));
        if (blob == null) throw new IOException("File not found: " + fileName);
        return new String(blob.getContent());
    }

    @Override
    public List<String> getAllFiles(String userId) {
        return getAllUserFiles(userId);
    }

    @Override
    public List<String> getFilesForUser(String userId) {
        return getAllUserFiles(userId);
    }

    @Override
    public List<String> getAllUserFiles(String userId) {
        List<String> fileNames = new ArrayList<>();
        String prefix = String.format("users/%s/", userId);

        bucket.list(com.google.cloud.storage.Storage.BlobListOption.prefix(prefix))
                .iterateAll()
                .forEach(blob -> {
                    String path = blob.getName();
                    if (!path.equals(prefix) && !path.isEmpty()) {
                        fileNames.add(path.substring(path.lastIndexOf('/') + 1));
                    }
                });

        return fileNames;
    }
}