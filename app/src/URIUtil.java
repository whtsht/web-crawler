import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class URIUtil {
    public static int getUriDepth(URI uri) {
        return (uri.getPath() + " ").split("/").length;
    }

    public static String htmlUriToFilename(URI uri) {
        if (uri.getPath().endsWith(".html")) {
            return "resources/" + uri.getHost() + uri.getPath();
        } else {
            return "resources/" + uri.getHost() + uri.getPath() + "index.html";
        }
    }

    public static String contentUriToFilename(URI uri) {
        return "resources/" + uri.getHost() + uri.getPath();
    }

    public static String htmlUriToLink(URI srcUri, URI dstUri) {
        return "../".repeat(getUriDepth(srcUri) + 1) + htmlUriToFilename(dstUri);
    }

    public static String contentUriToLink(URI srcUri, URI dstUri) {
        return "../".repeat(getUriDepth(srcUri) + 1) + contentUriToFilename(dstUri);
    }

    public static Optional<URI> getBaseUri(URI uri) {
        try {
            return Optional.of(
                    new URI(uri.getScheme().toString() + "://" + uri.getAuthority().toString() + "/"));
        } catch (URISyntaxException | NullPointerException ex) {
            return Optional.empty();
        }
    }

    public static URI absolute(URI baseUri, URI uri) {
        return baseUri.resolve(uri.normalize());
    }
}
