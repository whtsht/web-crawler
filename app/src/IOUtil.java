import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

public class IOUtil {
    public static final Duration HTTP_REQUEST_TIMEOUT = Duration.ofSeconds(5);

    public static Optional<HttpResponse<InputStream>> downloadContent(String uri) {
        final var client = HttpClient.newHttpClient();

        try {
            final var request = HttpRequest.newBuilder().uri(new URI(uri)).GET().timeout(HTTP_REQUEST_TIMEOUT).build();
            return Optional.of(client.send(request, HttpResponse.BodyHandlers.ofInputStream()));
        } catch (IOException
                | InterruptedException
                | URISyntaxException
                | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    public static void saveFile(String input, String path) {
        try (var outputStream = new PrintWriter(path)) {
            outputStream.print(input);
        } catch (IOException ex) {
            System.err.println("saveFile: " + ex.getMessage());
        }
    }

    public static void saveFile(InputStream input, String path) {
        try (var outputStream = new PrintWriter(path)) {
            outputStream.print(input);
        } catch (IOException ex) {
            System.err.println("saveFile: " + ex.getMessage());
        }
    }
}
