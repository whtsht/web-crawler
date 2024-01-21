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

    public static HttpRequest.Builder fakeHttpRequest() {
        return HttpRequest.newBuilder().headers(
                "accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8",
                "accept-language", "ja;q=0.5",
                "sec-ch-ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Brave\";v=\"120\"",
                "sec-ch-ua-mobile", "?0",
                "sec-ch-ua-model", "\"\"",
                "sec-ch-ua-platform", "\"Linux\"",
                "sec-ch-ua-platform-version", "\"6.7.0\"",
                "sec-fetch-dest", "document",
                "sec-fetch-mode", "navigate",
                "sec-fetch-site", "cross-site",
                "sec-fetch-user", "?1",
                "sec-gpc", "1",
                "upgrade-insecure-requests", "1",
                "user-agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
    }

    public static Try<HttpResponse<InputStream>> downloadContent(URI uri) {
        return Try.of(() -> {
            final var client = HttpClient.newHttpClient();
            final var request = fakeHttpRequest().uri(uri)
                    .GET().timeout(HTTP_REQUEST_TIMEOUT).build();
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
