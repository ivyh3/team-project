package use_case.upload_reference_material;

import java.io.File;
import java.io.IOException;

// StorageService interface
public interface StorageService {
    String upload(File content, String mimeType, String filename) throws IOException;
}
