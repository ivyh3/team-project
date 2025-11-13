package entity;

import java.time.LocalDateTime;

/**
 * Represents reference material (textbooks, PDFs, etc.) uploaded by a user.
 */
public class ReferenceMaterial {
    private String id;
    private String ownerUid;
    private String storagePath;
    private String filename;
    private long sizeBytes;
    private LocalDateTime uploadedAt;
    private String fingerprint;
    
    public ReferenceMaterial(String id, String ownerUid, String storagePath, String filename, 
                           long sizeBytes, LocalDateTime uploadedAt, String fingerprint) {
        this.id = id;
        this.ownerUid = ownerUid;
        this.storagePath = storagePath;
        this.filename = filename;
        this.sizeBytes = sizeBytes;
        this.uploadedAt = uploadedAt;
        this.fingerprint = fingerprint;
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getOwnerUid() {
        return ownerUid;
    }
    
    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }
    
    public String getStoragePath() {
        return storagePath;
    }
    
    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }
    
    public String getFilename() {
        return filename;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    public long getSizeBytes() {
        return sizeBytes;
    }
    
    public void setSizeBytes(long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }
    
    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
    
    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
    
    public String getFingerprint() {
        return fingerprint;
    }
    
    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}

