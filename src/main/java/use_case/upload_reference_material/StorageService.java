package use_case.upload_reference_material;

import java.io.File;
import java.io.IOException;

/**
 * Minimal service interface for file storage operations.
 * Implementations handle uploading files and may throw IOException.
 */
public interface StorageService {

    /**
     * Uploads a file and returns the storage path or URL.
     *
     * @param content  the file to upload
     * @param mimeType the MIME type of the file
     * @param filename the desired filename in storage
     * @return the path or URL where the file is stored
     * @throws IOException if an error occurs during upload
     */
    String upload(File content, String mimeType, String filename) throws IOException;

    /**
     * Deletes a file from storage.
     *
     * @param path the storage path or URL of the file to delete
     * @throws IOException if an error occurs during deletion
     */
    default void delete(String path) throws IOException {
        // optional default method; implementations may override
        throw new UnsupportedOperationException("Delete operation not implemented");
    }
}
