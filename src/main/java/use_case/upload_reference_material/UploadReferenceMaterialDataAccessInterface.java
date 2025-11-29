package use_case.upload_reference_material;

import java.io.File;
import java.util.List;

public interface UploadReferenceMaterialDataAccessInterface {
    String uploadFile(String userId, File file) throws Exception;
    void saveMetadata(String userId, String storagePath, String prompt) throws Exception;
    void deleteFile(String userId, String fileName) throws Exception;
    List<String> getAllFiles(String userId) throws Exception;
}
