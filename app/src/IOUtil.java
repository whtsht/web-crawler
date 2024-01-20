import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import io.vavr.control.Try;

public class IOUtil {
    public static final Duration HTTP_REQUEST_TIMEOUT = Duration.ofSeconds(5);

    public static Try<HttpResponse<InputStream>> downloadContent(URI uri) {
        return Try.of(() -> {
            final var client = HttpClient.newHttpClient();
            final var request = HttpRequest.newBuilder().uri(uri).GET().timeout(HTTP_REQUEST_TIMEOUT).build();
            return client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        });
    }

    public static Try<Void> saveFile(String input, String path) {
        return Try.of(() -> {
            try (var outputStream = new PrintWriter(path)) {
                outputStream.print(input);
                return null;
            }
        });
    }

    public static Try<Void> saveFile(InputStream input, String path) {
        return Try.of(() -> {
            try (var outputStream = new PrintWriter(path)) {
                outputStream.print(input);
                return null;
            }
        });
    }
}
