package frameworks_drivers.storage;

/**
 * Minimal storage service interface used by the interactor.
 * Implementations should delete the file at the given path and may throw on failure.
 */
public interface StorageService {
    /**
     * Delete the file at the given storage path.
     *
     * @param filePath path of the file to delete
     * @throws StorageException if deletion fails
     */
    void deleteFile(String filePath) throws StorageException;
}
