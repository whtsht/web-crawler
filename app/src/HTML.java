import java.net.URI;
import org.jsoup.nodes.Document;

public class HTML {
    public Document document;
    public URI baseUri;

    public HTML(Document document, URI baseUri) {
        this.document = document;
        this.baseUri = baseUri;
    }
}
