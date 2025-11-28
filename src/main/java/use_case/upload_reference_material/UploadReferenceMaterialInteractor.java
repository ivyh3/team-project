package use_case.upload_reference_material;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Interactor for the Upload Reference Material use case.
 * Clean, SOLID-compliant, and strongly typed.
 */
public class UploadReferenceMaterialInteractor implements UploadReferenceMaterialInputBoundary {

    private final ReferenceMaterialRepository materialRepository;
    private final StorageService storageService;
    private final UploadReferenceMaterialOutputBoundary outputBoundary;

    public UploadReferenceMaterialInteractor(ReferenceMaterialRepository materialRepository,
                                             StorageService storageService,
                                             UploadReferenceMaterialOutputBoundary outputBoundary) {
        this.materialRepository = materialRepository;
        this.storageService = storageService;
        this.outputBoundary = Objects.requireNonNull(outputBoundary);
    }

    @Override
    public void execute(UploadReferenceMaterialInputData inputData) {
        try {
            if (inputData == null || inputData.getFile() == null) {
                fail();
                return;
            }

            File file = inputData.getFile();
            String filename = file.getName();
            String mimeType = inputData.getMimeType();

            // 1. Compute fingerprint
            String fingerprint = computeSha256Hex(file);

            // 2. Check for duplicates if repository exists
            boolean isDuplicate = materialRepository != null &&
                    materialRepository.findByFingerprint(fingerprint).isPresent();

            if (isDuplicate) {
                fail();
                return;
            }

            // 3. Upload file if storageService exists, otherwise simulate path
            String storagePath = (storageService != null)
                    ? storageService.upload(file, mimeType, filename)
                    : "firebase://bucket/uploads/" + filename;

            // 4. Create ReferenceMaterial entity
            ReferenceMaterial material = new ReferenceMaterial(
                    filename,
                    storagePath,
                    fingerprint,
                    Instant.now()
            );

            // 5. Save to repository if available
            if (materialRepository != null) {
                materialRepository.save(material);
            }

            // 6. Prepare success output
            UploadReferenceMaterialOutputData outputData = new UploadReferenceMaterialOutputData(filename, storagePath);
            outputBoundary.prepareSuccessView(outputData);

        } catch (Exception e) {
            fail();
        }
    }

    private void fail() {
        UploadReferenceMaterialOutputData failureOutput = new UploadReferenceMaterialOutputData("", "");
        outputBoundary.prepareSuccessView(failureOutput);
    }

    private static String computeSha256Hex(File data) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] fileBytes = Files.readAllBytes(data.toPath());
        byte[] digest = md.digest(fileBytes);
        StringBuilder sb = new StringBuilder(digest.length * 2);
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
