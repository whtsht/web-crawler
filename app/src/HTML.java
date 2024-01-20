import java.net.URI;
import org.jsoup.nodes.Document;

public class HTML {
    public Document document;
    public URI srcUri;
    public URI baseUri;

    public HTML(Document document, URI srcUri, URI baseUri) {
        this.document = document;
        this.srcUri = srcUri;
        this.baseUri = baseUri;
    }
}
