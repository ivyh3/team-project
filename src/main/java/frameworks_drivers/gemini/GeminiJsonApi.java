package frameworks_drivers.gemini;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Question;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;
import java.util.Base64;

public class GeminiJsonApi implements GeminiDataAccess {
    private final Path root = Paths.get("data/gemini");
    private final Path requests = root.resolve("requests");
    private final Path responses = root.resolve("responses");
    private final Path exports = root.resolve("exports");
    private final Path filesManifest = root.resolve("files.json");
    private final ObjectMapper mapper = new ObjectMapper();

    // Polling configuration for Option A (wait for response file)
    private static final long RESPONSE_WAIT_TIMEOUT_MS = 30_000; // total wait time
    private static final long RESPONSE_POLL_INTERVAL_MS = 500;   // poll interval

    // Callback receiver configuration (port configurable via env var)
    private static final int DEFAULT_CALLBACK_PORT = 8080;
    private final int callbackPort;

    public GeminiJsonApi() throws IOException {
        Files.createDirectories(requests);
        Files.createDirectories(responses);
        Files.createDirectories(exports);
        if (Files.notExists(filesManifest)) {
            mapper.writeValue(filesManifest.toFile(), new HashMap<String, Map<String, Object>>());
        }

        String portEnv = System.getenv().getOrDefault("GEMINI_CALLBACK_PORT", Integer.toString(DEFAULT_CALLBACK_PORT));
        int port;
        try {
            port = Integer.parseInt(portEnv);
        } catch (NumberFormatException e) {
            port = DEFAULT_CALLBACK_PORT;
        }
        this.callbackPort = port;

        // start local HTTP receiver for guaranteed automated flow (external service should POST to the callback)
        try {
            GeminiResponseReceiver.startIfNeeded(callbackPort, responses);
        } catch (IOException e) {
            // If receiver can't start, continue — polling and fallback still work
        }
    }

    private String writeRequestAndReturnId(Map<String, Object> payload) throws IOException {
        String id = UUID.randomUUID().toString();
        payload.put("timestamp", Instant.now().toString());
        // include callback URL so external service can POST the response to responses/<id>.json
        String callbackUrl = "http://127.0.0.1:" + callbackPort + "/response/" + id;
        payload.put("callbackUrl", callbackUrl);
        Path file = requests.resolve(id + ".json");
        mapper.writeValue(file.toFile(), payload);
        return id;
    }

    private Optional<Path> findResponseForId(String id) {
        Path resp = responses.resolve(id + ".json");
        return Files.exists(resp) ? Optional.of(resp) : Optional.empty();
    }

    private Optional<Path> findAnyResponse() {
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(responses, "*.json")) {
            for (Path p : ds) return Optional.of(p);
        } catch (IOException ignored) {}
        return Optional.empty();
    }

    // wait for responses/<id>.json to appear up to a timeout
    private Optional<Path> waitForResponseFile(String id) {
        long waited = 0L;
        Path target = responses.resolve(id + ".json");
        while (waited < RESPONSE_WAIT_TIMEOUT_MS) {
            if (Files.exists(target)) {
                return Optional.of(target);
            }
            try {
                Thread.sleep(RESPONSE_POLL_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return Optional.empty();
            }
            waited += RESPONSE_POLL_INTERVAL_MS;
        }
        return Optional.empty();
    }

    @Override
    public List<Question> generateQuiz(String prompt, List<String> referenceMaterialTexts) throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("type", "generateQuiz");
        req.put("prompt", prompt);
        req.put("references", referenceMaterialTexts);
        return getQuestions(req);
    }

    private List<Question> getQuestions(Map<String, Object> req) throws IOException {
        String id = writeRequestAndReturnId(req);

        // wait for the external service (or local callback) to write responses/<id>.json
        Optional<Path> resp = waitForResponseFile(id);
        if (resp.isEmpty()) {
            // fallback to any prepared response file for local testing
            resp = findAnyResponse();
        }

        if (resp.isPresent()) {
            Map<String, Object> data = mapper.readValue(resp.get().toFile(), new TypeReference<>() {
            });
            Object rawQuestions = data.get("questions");
            if (rawQuestions == null) return Collections.emptyList();
            // convert explicitly to List<Question> so fields like correctAnswer/explanation map properly
            return mapper.convertValue(rawQuestions, new TypeReference<>() {
            });
        }
        return Collections.emptyList();
    }

    @Override
    public List<Question> generateQuizWithFiles(String prompt, List<String> fileUris) throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("type", "generateQuizWithFiles");
        req.put("prompt", prompt);
        req.put("fileUris", fileUris);
        return getQuestions(req);
    }

    public Path generateQuizAndExport(String prompt, List<String> referenceMaterialTexts) throws Exception {
        List<Question> questions = generateQuiz(prompt, referenceMaterialTexts);
        String exportId = UUID.randomUUID().toString();
        Path out = exports.resolve(exportId + ".json");
        mapper.writeValue(out.toFile(), questions);
        return out;
    }

    public Path generateQuizWithFilesAndExport(String prompt, List<String> fileUris) throws Exception {
        List<Question> questions = generateQuizWithFiles(prompt, fileUris);
        String exportId = UUID.randomUUID().toString();
        Path out = exports.resolve(exportId + ".json");
        mapper.writeValue(out.toFile(), questions);
        return out;
    }

    @Override
    public String uploadFileToGemini(byte[] fileContent, String mimeType, String displayName) throws Exception {
        Map<String, Map<String, Object>> manifest = mapper.readValue(filesManifest.toFile(),
                new TypeReference<>() {
                });
        String uri = "file://" + UUID.randomUUID();
        Map<String, Object> entry = new HashMap<>();
        entry.put("displayName", displayName);
        entry.put("mimeType", mimeType);
        entry.put("uploadedAt", Instant.now().toString());
        entry.put("contentBase64", Base64.getEncoder().encodeToString(fileContent));
        manifest.put(uri, entry);
        mapper.writeValue(filesManifest.toFile(), manifest);
        return uri;
    }

    @Override
    public void deleteFileFromGemini(String fileUri) throws Exception {
        Map<String, Map<String, Object>> manifest = mapper.readValue(filesManifest.toFile(),
                new TypeReference<>() {
                });
        if (manifest.remove(fileUri) != null) {
            mapper.writeValue(filesManifest.toFile(), manifest);
        } else {
            throw new IOException("fileUri not found: " + fileUri);
        }
    }

    @Override
    public String generateText(String prompt) throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("type", "generateText");
        req.put("prompt", prompt);
        String id = writeRequestAndReturnId(req);

        // wait for the external service to write responses/<id>.json (or callback to deliver it)
        Optional<Path> resp = waitForResponseFile(id);
        if (resp.isEmpty()) {
            resp = findAnyResponse();
        }

        if (resp.isPresent()) {
            Map<String, Object> data = mapper.readValue(resp.get().toFile(), new TypeReference<>() {
            });
            Object text = data.get("text");
            return text == null ? "" : text.toString();
        }
        return "";
    }
}
