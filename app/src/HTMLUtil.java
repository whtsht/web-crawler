import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
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

    public static List<LinkedElement> extractSrc(Document doc) {
        return null;
    }

    public static List<LinkedElement> extractHref(Document doc) {
        return null;
    }
}
