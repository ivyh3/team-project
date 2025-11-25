package use_case.upload_reference_material;

// ReferenceMaterialRepository interface
public interface ReferenceMaterialRepository {
    ReferenceMaterial findByFingerprint(String fingerprint);
    void save(ReferenceMaterial material);
}
