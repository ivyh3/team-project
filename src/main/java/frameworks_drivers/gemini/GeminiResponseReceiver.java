package frameworks_drivers.gemini;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Abstraction for receiving AI responses from Gemini.
 * Allows the use case layer to depend on this interface instead of a concrete HTTP server.
 */
public interface GeminiResponseReceiver {
    /**
     * Starts the server to receive responses, saving them under the given directory.
     *
     * @param port         the port to listen on
     * @param responsesDir the directory to store received responses
     * @throws IOException if server cannot start
     */
    void start(int port, Path responsesDir) throws IOException;

    /**
     * Stops the server.
     */
    void stop();
}
