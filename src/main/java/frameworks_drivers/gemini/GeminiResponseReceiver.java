package frameworks_drivers.gemini;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GeminiResponseReceiver {
    private static HttpServer server;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Start a local HTTP server bound to 127.0.0.1:{port} that accepts POST /response/{id}.
     * The POST body is written atomically to responsesDir/{id}.json.
     */
    public static synchronized void startIfNeeded(int port, Path responsesDir) throws IOException {
        if (server != null) return;

        Files.createDirectories(responsesDir);
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
        server.createContext("/response", new ResponseHandler(responsesDir));
        Executor executor = Executors.newCachedThreadPool();
        server.setExecutor(executor);
        server.start();
    }

    static class ResponseHandler implements HttpHandler {
        private final Path responsesDir;

        ResponseHandler(Path responsesDir) {
            this.responsesDir = responsesDir;
        }

        @Override
        public void handle(HttpExchange exchange) {
            try (exchange) {
                if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(405, -1);
                    return;
                }

                String path = exchange.getRequestURI().getPath(); // expected: /response/{id}
                String[] parts = path.split("/");
                if (parts.length < 3 || parts[2].isEmpty()) {
                    exchange.sendResponseHeaders(400, -1);
                    return;
                }
                String id = parts[2];

                try (InputStream is = exchange.getRequestBody()) {
                    byte[] body = is.readAllBytes();
                    // write to tmp then move atomically
                    Path tmp = responsesDir.resolve(id + ".json.tmp");
                    Path out = responsesDir.resolve(id + ".json");
                    Files.write(tmp, body);
                    try {
                        Files.move(tmp, out, java.nio.file.StandardCopyOption.ATOMIC_MOVE, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        // Fallback to non-atomic move if atomic not supported
                        Files.move(tmp, out, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                }

                byte[] ok = "ok".getBytes();
                exchange.sendResponseHeaders(200, ok.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(ok);
                }
            } catch (Exception ignored) {
            }
        }
    }
}
