package frameworks_drivers.firebase;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import use_case.upload_reference_material.UploadReferenceMaterialDataAccessInterface;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class FirebaseUploadRepository implements UploadReferenceMaterialDataAccessInterface {

    private final Firestore firestore = FirestoreClient.getFirestore();

    @Override
    public String uploadFile(String userId, File file) throws Exception {
        String fileId = UUID.randomUUID().toString();
        String storageName = "materials/" + userId + "/" + fileId + ".pdf";

        StorageClient.getInstance()
                .bucket()
                .create(storageName, new FileInputStream(file), "application/pdf");

        return storageName;
    }

    @Override
    public void saveMetadata(String userId, String storagePath, String prompt) throws Exception {
        Map<String, Object> meta = new HashMap<>();
        meta.put("storagePath", storagePath);
        meta.put("prompt", prompt);
        meta.put("timestamp", System.currentTimeMillis());

        firestore.collection("users")
                .document(userId)
                .collection("materials")
                .add(meta)
                .get();
    }

    @Override
    public void deleteFile(String userId, String storagePath) throws Exception {
        // Delete from Firebase Storage
        StorageClient.getInstance().bucket().get(storagePath).delete();

        // Delete Firestore metadata
        firestore.collection("users")
                .document(userId)
                .collection("materials")
                .whereEqualTo("storagePath", storagePath)
                .get()
                .get()
                .getDocuments()
                .forEach(doc -> doc.getReference().delete());
    }

    @Override
    public List<String> getAllFiles(String userId) throws Exception {
        return firestore.collection("users")
                .document(userId)
                .collection("materials")
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(doc -> doc.getString("storagePath"))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
