package use_case.upload_reference_material;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain entity representing a reference material.
 */
public final class ReferenceMaterial {
    private final String filename;
    private final String storagePath;
    private final String fingerprint;
    private final Instant createdAt;

    public ReferenceMaterial(String filename, String storagePath, String fingerprint, Instant createdAt) {
        this.filename = Objects.requireNonNull(filename, "filename cannot be null");
        this.storagePath = Objects.requireNonNull(storagePath, "storagePath cannot be null");
        this.fingerprint = Objects.requireNonNull(fingerprint, "fingerprint cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt cannot be null");
    }

    public String getFilename() {
        return filename;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReferenceMaterial)) return false;
        ReferenceMaterial that = (ReferenceMaterial) o;
        return filename.equals(that.filename) &&
                storagePath.equals(that.storagePath) &&
                fingerprint.equals(that.fingerprint) &&
                createdAt.equals(that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, storagePath, fingerprint, createdAt);
    }

    @Override
    public String toString() {
        return "ReferenceMaterial{" +
                "filename='" + filename + '\'' +
                ", storagePath='" + storagePath + '\'' +
                ", fingerprint='" + fingerprint + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
