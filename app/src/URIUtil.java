import java.net.URI;

import io.vavr.control.Try;

public class URIUtil {
    public static int getUriDepth(URI uri) {
        return (uri.getPath() + " ").split("/").length;
    }

    public static String htmlUriToFilename(URI uri) {
        var path = "resources/" + uri.getHost() + uri.getPath();
        if (uri.getPath().endsWith(".html")) {
            return path;
        } else {
            return path.endsWith("/") ? path + "index.html" : path + "/index.html";
        }
    }

    public static String contentUriToFilename(URI uri) {
        return "resources/" + uri.getHost() + uri.getPath();
    }

    public static String htmlUriToLink(URI srcUri, URI dstUri) {
        return "../".repeat(getUriDepth(srcUri)) + htmlUriToFilename(dstUri);
    }

    public static String contentUriToLink(URI srcUri, URI dstUri) {
        return "../".repeat(getUriDepth(srcUri)) + contentUriToFilename(dstUri);
    }

    public static Try<URI> getBaseUri(URI uri) {
        return Try.of(() -> new URI(uri.getScheme().toString() + "://" + uri.getAuthority().toString()));
    }

    public static URI absolute(URI baseUri, URI uri) {
        return baseUri.resolve(uri.normalize());
    }

    public static Try<URI> normalize(URI uri) {

        return Try.of(() -> {
            var uri_ = new URI(getBaseUri(uri).get() + uri.getPath());
            return uri_.getPath().length() == 0 ? new URI(uri_.toString() + "/") : uri_;
        });
    }
}
