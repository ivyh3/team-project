package entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain entity representing reference material uploaded by a user.
 * Immutable after creation to maintain domain integrity.
 */
public class ReferenceMaterial {

    private final String id;
    private final String ownerUid;
    private final String storagePath;
    private final String filename;
    private final long sizeBytes;
    private final LocalDateTime uploadedAt;
    private final String fingerprint;

    public ReferenceMaterial(
            String id,
            String ownerUid,
            String storagePath,
            String filename,
            long sizeBytes,
            LocalDateTime uploadedAt,
            String fingerprint
    ) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.ownerUid = Objects.requireNonNull(ownerUid, "ownerUid cannot be null");
        this.storagePath = Objects.requireNonNull(storagePath, "storagePath cannot be null");
        this.filename = Objects.requireNonNull(filename, "filename cannot be null");
        this.fingerprint = Objects.requireNonNull(fingerprint, "fingerprint cannot be null");
        this.sizeBytes = sizeBytes;
        this.uploadedAt = uploadedAt == null ? LocalDateTime.now() : uploadedAt;
    }

    public String getId() {
        return id;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getFilename() {
        return filename;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public String getFingerprint() {
        return fingerprint;
    }
}
