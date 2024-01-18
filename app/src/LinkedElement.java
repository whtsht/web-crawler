import java.net.URI;
import java.util.Optional;
import org.jsoup.nodes.Element;

public class LinkedElement {
    private String uri;
    private Element elem;
    private String attrName;

    public LinkedElement(String attrName, Element elem) {
        this.uri = elem.attr(attrName);
        this.attrName = attrName;
        this.elem = elem;
    }

    public void rewriteURI(String path) {
        elem.attr(attrName, path);
    }

    // if newUri = normalizeURI(LinkedElemtnt.uri)
    // - return LinkedElemtnt.rewrite(URIToLink(newUri))
    // else
    // - return Optional.empty()
    public Optional<URI> normalizeURI() {
        return Optional.empty();
    }
}
