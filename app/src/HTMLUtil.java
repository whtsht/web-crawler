import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.stream.Stream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLUtil {
    public static boolean isHtml(HttpResponse<InputStream> response) {
        final var headers = response.headers();
        try {
            return headers.map().get("content-type").get(0).contains("text/html");
        } catch (NullPointerException ex) {
            return false;
        }
    }

    public static Optional<Document> parseHtml(HttpResponse<InputStream> response, String baseUri) {
        if (HTMLUtil.isHtml(response)) {
            try {
                return Optional.of(Jsoup.parse(response.body(), null, baseUri));
            } catch (IOException ex) {
            }
        }
        return Optional.empty();
    }

    public static Stream<LinkedElement> extractSrc(Document doc) {
        var sources = doc.select("[src]");
        return sources.stream().map(source -> new LinkedElement("src", source));
    }

    public static Stream<LinkedElement> extractHref(Document doc) {
        var links = doc.select("[href]");
        return links.stream().map(link -> new LinkedElement("href", link));
    }
}
