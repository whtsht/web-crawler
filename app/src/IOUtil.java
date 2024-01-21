import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.io.File;
import io.vavr.control.Try;

public class IOUtil {
    public static final Duration HTTP_REQUEST_TIMEOUT = Duration.ofSeconds(3);

    public static class Content {
        private Object input;
        private String path;

        public Content(Object input, String path) {
            this.input = input;
            this.path = path;
        }

    }

    public static Try<Void> saveFile(Content content) {
        return Try.of(() -> {
            var file = new File(content.path);
            file.getParentFile().mkdirs();
            try (var outputStream = new PrintWriter(file)) {
                outputStream.print(content.input);
                return null;
            }
        });
    }

    public static Try<HttpResponse<InputStream>> downloadContent(URI uri) {
        return Try.of(() -> {
            final var client = HttpClient.newHttpClient();
            final var request = HttpRequest.newBuilder().uri(uri).GET().timeout(HTTP_REQUEST_TIMEOUT).build();
            return client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        });
    }

    public static void deleteDirectory(File directory) {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            }
            file.delete();
        }
        directory.delete();
    }
}
