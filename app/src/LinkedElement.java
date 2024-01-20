import java.net.URI;
import java.util.function.Function;
import org.jsoup.nodes.Element;
import io.vavr.control.Try;

public class LinkedElement {
    private URI uri;
    private Element elem;
    private String attrName;

    public LinkedElement(URI uri, Element elem, String attrName) {
        this.uri = uri;
        this.attrName = attrName;
        this.elem = elem;
    }

    public static Function<Element, Try<LinkedElement>> of(String attrName) {
        return elem -> Try.of(() -> new LinkedElement(new URI(elem.attr(attrName)), elem, attrName));
    }

    public void rewriteURI(String path) {
        elem.attr(attrName, path);
    }

    public URI applyNewUri(URI baseUri, Function<URI, String> replaceFunction) {
        final var newUri = URIUtil.absolute(baseUri, uri);
        rewriteURI(replaceFunction.apply(newUri));
        return newUri;
    }
}
