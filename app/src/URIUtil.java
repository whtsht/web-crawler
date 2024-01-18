import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;

public class URIUtil {
    // html:
    // filename: resources/<baseuri>/index.html
    // uri : ..{depth(<source_baseuri>)}/resources/<target_baseuri>/index.html
    // content: resources/<baseuri>/filename
    // uri : ..{depth(<source_baseuri>)}/resources/<target_baseuri>/filename
    public static int getUriDepth(URI uri) {
        return 0;
    }

    public static String htmlUriToFilename(URI uri) {
        return uri.toString();
    }

    public static String contentUriToFilename(URI uri) {
        return uri.toString();
    }

    public static String htmlUriToLink(URI uri) {
        return uri.toString();
    }

    public static String contentUriToLink(URI uri) {
        return uri.toString();
    }

    public static Optional<URI> getBaseUri(URI uri) {
        try {
            final var scheme = uri.getScheme();
            final var host = uri.getHost();
            if (Objects.nonNull(scheme) && Objects.nonNull(host)) {
                return Optional.of(new URI(scheme.toString() + "://" + host.toString() + "/"));
            }
        } catch (URISyntaxException ex) {
        }
        return Optional.empty();
    }

    public static Optional<URI> uriNormalization(URI baseUri, URI uri) {
        var uri_ = uri.toString();
        if (uri_.contains(":")) {
            // Absolute URI
            return Optional.of(uri);
        }

        if (uri_.length() >= 2 && uri_.substring(0, 2).equals("//")) {
            try {
                return Optional.of(new URI(baseUri.getScheme() + "://" + uri_.substring(2)));
            } catch (URISyntaxException ex) {
            }
        }

        if (uri_.length() >= 1 && uri_.substring(0, 1).equals("/")) {
            try {
                return Optional.of(new URI(baseUri + uri_.substring(1)));
            } catch (URISyntaxException ex) {
            }
        }

        return Optional.empty();
    }
}
