package frameworks_drivers.storage;

/**
 * Minimal storage service interface used by the interactor.
 * Implementations should delete the file at the given path and may throw on failure.
 */
public interface StorageService {
    /**
     * Delete the file at the given storage path.
     */
    void deleteFile(String filePath);
}
