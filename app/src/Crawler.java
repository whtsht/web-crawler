import java.net.URI;
import io.vavr.collection.List;
import java.util.Optional;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.function.Function;

public class Crawler {
    public static Optional<HTML> pullContents(HttpResponse<InputStream> response, URI uri, URI baseUri) {
        if (HTMLUtil.isHtml(response)) {
            return HTMLUtil.parseHtml(response, uri, baseUri).toJavaOptional();
        } else {
            System.out.println(IOUtil.saveFile(response.body(), URIUtil.contentUriToFilename(uri)));
            return Optional.empty();
        }
    }

    public static List<URI> replaceUri(HTML html) {
        final Function<String, List<URI>> replaceUriWithAttr = attributeName -> HTMLUtil
                .replaceUri(attributeName, HTMLUtil.hyperLink(html.srcUri)).apply(html);

        return List.of("src", "href").flatMap(replaceUriWithAttr);
    }

    public static void saveDocument(HTML html) {
        IOUtil.saveFile(html.document.outerHtml(), URIUtil.htmlUriToFilename(html.baseUri));
    }

    public static List<URI> crawlingOneStep(List<URI> uriList) {
        return uriList;
    }

    public static void crawlingWithDepth(int depth, URI uri) {
    }
}
