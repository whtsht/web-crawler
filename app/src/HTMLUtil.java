import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.function.Function;
import org.jsoup.Jsoup;
import io.vavr.collection.List;
import io.vavr.control.Try;

public class HTMLUtil {
    public static boolean isHtml(HttpResponse<InputStream> response) {
        final var headers = response.headers();
        try {
            return headers.map().get("content-type").get(0).contains("text/html");
        } catch (NullPointerException ex) {
            return false;
        }
    }

    public static boolean isHtml(URI uri) {
        final var content = IOUtil.downloadContent(uri);
        return content.map(response -> isHtml(response)).getOrElse(false);
    }

    public static Try<Optional<HTML>> parseHtml(HttpResponse<InputStream> response, URI baseUri) {
        return Try.of(() -> {
            if (HTMLUtil.isHtml(response)) {
                final var document = Jsoup.parse(response.body(), null, baseUri.toString());
                return Optional.of(new HTML(document, baseUri));
            } else {
                return Optional.empty();
            }
        });
    }

    public static Function<HTML, List<URI>> replaceUri(String attributeName, Function<URI, URI> replaceFunction) {
        return html -> {
            return List.ofAll(html.document.select("[" + attributeName + "]"))
                    .flatMap(LinkedElement.of(attributeName))
                    .map(elem -> elem.applyNewUri(html.baseUri, replaceFunction));
        };
    }
}
