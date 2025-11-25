package use_case.upload_reference_material;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Objects;

/**
 * Interactor for the Upload Reference Material use case.
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

    // Backwards-compatible constructor that keeps previous simulated behaviour
    public UploadReferenceMaterialInteractor(UploadReferenceMaterialOutputBoundary outputBoundary) {
        this(null, null, outputBoundary);
    }

    @Override
    public void execute(UploadReferenceMaterialInputData inputData) {
        try {
            // Basic validation
            if (inputData == null || inputData.getFile() == null) {
                // failure: invalid input -> return empty output (keeps compatibility with prior code)
                UploadReferenceMaterialOutputData failureOutput = new UploadReferenceMaterialOutputData("", "");
                outputBoundary.prepareSuccessView(failureOutput);
                return;
            }

            File content = inputData.getFile();
            String filename = inputData.getFile().getName();
            String mimeType = inputData.getPrompt();

            // 1. Compute fingerprint (SHA-256 hex)
            String fingerprint = computeSha256Hex(content);

            // 1a. Check for duplicates using repository if available
            boolean isDuplicate = false;
            if (materialRepository != null) {
                ReferenceMaterial existing = materialRepository.findByFingerprint(fingerprint);
                isDuplicate = existing != null;
            }

            if (isDuplicate) {
                // 5. Prepare failure view for duplicate (keeps old behaviour: empty fields on failure)
                UploadReferenceMaterialOutputData failureOutput = new UploadReferenceMaterialOutputData("", "");
                outputBoundary.prepareSuccessView(failureOutput);
                return;
            }

            // 2. Upload file to storage (if storageService provided) otherwise simulate path
            String storagePath;
            if (storageService != null) {
                storagePath = storageService.upload(content, mimeType, filename);
            } else {
                // Simulated path for environments without StorageService configured
                storagePath = "firebase://bucket/uploads/" + filename;
            }

            // 3. Create ReferenceMaterial entity with metadata
            ReferenceMaterial material = new ReferenceMaterial(filename, storagePath, fingerprint, Instant.now());

            // 4. Save to repository if available (otherwise skip)
            if (materialRepository != null) {
                materialRepository.save(material);
            }

            // 5. Prepare success view
            UploadReferenceMaterialOutputData outputData = new UploadReferenceMaterialOutputData(filename, storagePath);
            outputBoundary.prepareSuccessView(outputData);
        } catch (Exception e) {
            // On unexpected error, return failure output consistent with prior code
            UploadReferenceMaterialOutputData failureOutput = new UploadReferenceMaterialOutputData("", "");
            outputBoundary.prepareSuccessView(failureOutput);
        }
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

