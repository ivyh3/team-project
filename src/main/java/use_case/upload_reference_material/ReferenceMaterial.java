package use_case.upload_reference_material;

import java.time.Instant;

// ReferenceMaterial class
public class ReferenceMaterial {
    private String filename;
    private String storagePath;
    private String fingerprint;
    private Instant createdAt;

    public ReferenceMaterial(String filename, String storagePath, String fingerprint, Instant createdAt) {
        this.filename = filename;
        this.storagePath = storagePath;
        this.fingerprint = fingerprint;
        this.createdAt = createdAt;
    }

    // Getters and setters (if needed)
}
