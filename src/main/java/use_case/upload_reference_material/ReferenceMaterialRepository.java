package use_case.upload_reference_material;

import java.util.Optional;

public interface ReferenceMaterialRepository {
    Optional<ReferenceMaterial> findByFingerprint(String fingerprint);
    void save(ReferenceMaterial material);
}
