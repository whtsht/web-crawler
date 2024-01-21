import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import io.vavr.control.Try;

public class IOUtil {
    public static final Duration HTTP_REQUEST_TIMEOUT = Duration.ofSeconds(3);

    public static class Content {
        private InputStream input;
        private String path;

        public Content(InputStream input, String path) {
            this.input = input;
            this.path = path;
        }

        public static Content of(InputStream input, String path) {
            return new Content(input, path);
        }

        public static Content of(String input, String path) {
            return new Content(new ByteArrayInputStream(input.getBytes()), path);
        }
    }

    public static Try<Void> saveFile(Content content) {
        return Try.of(() -> {
            var file = new File(content.path);
            file.getParentFile().mkdirs();

            try (var outputStream = new FileOutputStream(file)) {

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = content.input.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return null;
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
        final var listFiles = directory.listFiles();
        if (listFiles == null)
            return;

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            }
            file.delete();
        }
        directory.delete();
    }
}
