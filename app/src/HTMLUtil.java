import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;
import org.jsoup.Jsoup;
import io.vavr.collection.List;
import io.vavr.control.Try;

public class HTMLUtil {
    public static HttpRequest.Builder fakeHttpRequestHeader() {
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

    public static boolean isHtml(HttpResponse<InputStream> response) {
        final var headers = response.headers();
        try {
            return headers.map().get("content-type").get(0).contains("text/html");
        } catch (NullPointerException ex) {
            return false;
        }
    }

    public static boolean isHtml(URI uri) {
        if (uri.getPath() == null)
            return false;
        var components = List.of(uri.getPath().split("/"));
        return components.filter(component -> component.contains(".")).length() == 0
                || !components.last().contains(".")
                || components.last().contains(".html");
    }

    public static Try<HTML> parseHtml(HttpResponse<InputStream> response, URI srcUri, URI baseUri) {
        return Try.of(() -> new HTML(Jsoup.parse(response.body(), null, baseUri.toString()), srcUri, baseUri));
    }

    public static Function<URI, String> hyperLink(URI srcUri) {
        return dstUri -> isHtml(dstUri)
                ? URIUtil.htmlUriToLink(srcUri, dstUri)
                : URIUtil.contentUriToLink(srcUri, dstUri);
    }

    public static Function<HTML, List<URI>> replaceUri(String attributeName, Function<URI, String> replaceFunction) {
        return html -> {
            return List.ofAll(html.document.select("[" + attributeName + "]"))
                    .flatMap(LinkedElement.of(attributeName))
                    .map(elem -> elem.applyNewUri(html.baseUri, replaceFunction));
        };
    }
}
