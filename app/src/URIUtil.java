import java.net.URI;
import java.util.Optional;

public class URIUtil {
    // html:
    // filename: resources/<baseuri>/index.html
    // uri : ..{depth(<source_baseuri>)}/resources/<target_baseuri>/index.html
    // content: resources/<baseuri>/filename
    // uri : ..{depth(<source_baseuri>)}/resources/<target_baseuri>/filename
    public static String htmlURIToFilename(URI uri) {
        return uri.toString();
    }

    public static String contentURIToFilename(URI uri) {
        return uri.toString();
    }

    public static String htmlURIToLink(URI uri) {
        return uri.toString();
    }

    public static String contentURIToLink(URI uri) {
        return uri.toString();
    }

    public static Optional<URI> URINormalization(String baseUri, String uri) {
        return Optional.empty();
    }

    public static int getURIDepth(URI uri) {
        return 0;
    }
}
