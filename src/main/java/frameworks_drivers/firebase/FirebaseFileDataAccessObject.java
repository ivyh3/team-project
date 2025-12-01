package frameworks_drivers.firebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;

import app.Config;
import use_case.start_study_session.StartStudySessionDataAccessInterface;
import use_case.upload_reference_material.UploadReferenceMaterialDataAccessInterface;

public class FirebaseFileDataAccessObject implements UploadReferenceMaterialDataAccessInterface, StartStudySessionDataAccessInterface {

    private final String FILES_BUCKET;
    private final Bucket bucket;

    public FirebaseFileDataAccessObject() {
        this.FILES_BUCKET = Config.getFirebaseStorageBucket();

        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream serviceAccount = new FileInputStream(Config.getFirebaseCredentialsPath());

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setStorageBucket(FILES_BUCKET)
                        .build();

                FirebaseApp.initializeApp(options);
            }

            this.bucket = StorageClient.getInstance().bucket();
        } catch (IOException e) {
            throw new RuntimeException("Error initializing Firebase: " + e.getMessage(), e);
        }
    }

    public String uploadFile(String userId, File file) throws IOException {
        String fileName = file.getName();
        byte[] data = Files.readAllBytes(file.toPath());
        String mimeType = Files.probeContentType(file.toPath());
        if (mimeType == null) mimeType = "application/octet-stream";

        String filePath = String.format("users/%s/%s", userId, fileName);
        bucket.create(filePath, data, mimeType);

        return filePath;
    }

    public void deleteFile(String userId, String fileName) {
        String filePath = String.format("users/%s/%s", userId, fileName);
        BlobId blobId = BlobId.of(FILES_BUCKET, filePath);
        boolean deleted = bucket.getStorage().delete(blobId);
        if (!deleted) {
            throw new RuntimeException("File not found or could not be deleted: " + filePath);
        }
    }

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

    @Override
    public List<String> getAllFiles(String userId) {
        return getAllUserFiles(userId);
    }

    @Override
    public List<String> getFilesForUser(String userId) {
        return getAllUserFiles(userId);
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
        deleteFile(userId, fileName);

        Firestore firestore = FirestoreClient.getFirestore();
        String docPath = String.format("users/%s/%s", userId, fileName);
        firestore.collection("users")
                .document(userId)
                .collection("materials")
                .document(docPath)
                .delete()
                .get();
    }

    @Override
    public boolean fileExistsByName(String userId, String fileName) {
        String path = String.format("users/%s/%s", userId, fileName);
        return bucket.get(path) != null;
    }

    public String getFileContent(String userId, String fileName) throws IOException {
        String path = String.format("users/%s/%s", userId, fileName);
        Blob blob = bucket.get(path);
        if (blob == null) throw new IOException("File not found: " + path);
        return new String(blob.getContent());
    }
}