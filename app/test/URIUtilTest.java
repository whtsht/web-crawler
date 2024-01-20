import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

import io.vavr.control.Try;

class URIUtilTest {
    @Test
    void successTranslation() throws URISyntaxException {
        var uri = new URI("https://stackoverflow.com/jobs/companies");

        var baseUri = URIUtil.getBaseUri(uri);
        assertEquals(baseUri, Try.of(() -> new URI("https://stackoverflow.com/")));

        URI absoluteUri;
        absoluteUri = URIUtil.absolute(baseUri.get(), new URI("/users"));
        assertEquals(absoluteUri, new URI("https://stackoverflow.com/users"));

        absoluteUri = URIUtil.absolute(baseUri.get(), new URI("/"));
        assertEquals(absoluteUri, new URI("https://stackoverflow.com/"));

        absoluteUri = URIUtil.absolute(baseUri.get(), new URI("//stackoverflow.com/"));
        assertEquals(absoluteUri, new URI("https://stackoverflow.com/"));
    }

    @Test
    void getBaseUri() throws URISyntaxException {
        assertEquals(
                URIUtil.getBaseUri(new URI("https://datatracker.ietf.org/doc/html/rfc3986#section-4.2")),
                Try.of(() -> new URI("https://datatracker.ietf.org/")));
        assertTrue(URIUtil.getBaseUri(new URI("/doc/html/rfc3986#section-4.2")).isFailure());
        assertTrue(URIUtil.getBaseUri(new URI("//datatracker.ietf.org")).isFailure());
    }

    @Test
    void getUriDepth() throws URISyntaxException {
        assertEquals(URIUtil.getUriDepth(new URI("https://host/a/b.txt")), 3);
        assertEquals(URIUtil.getUriDepth(new URI("https://host/a/b/")), 4);
        assertEquals(URIUtil.getUriDepth(new URI("https://host/")), 2);
    }

    @Test
    void toFilename() throws URISyntaxException {
        assertEquals(URIUtil.htmlUriToFilename(new URI("https://b/")), "resources/b/index.html");
        assertEquals(URIUtil.htmlUriToFilename(new URI("https://b/sub.html")), "resources/b/sub.html");
        assertEquals(
                URIUtil.contentUriToFilename(new URI("https://a/b/c/script.js")),
                "resources/a/b/c/script.js");
        assertEquals(
                URIUtil.contentUriToFilename(
                        new URI(
                                "https://s.yimg.jp/images/ds/managed/1/managed-ual.min.js?tk=4465a92c-f0fd-406f-b519-efd409cc9849&service=toppage")),
                "resources/s.yimg.jp/images/ds/managed/1/managed-ual.min.js");
    }

    @Test
    void toLink() throws URISyntaxException {
        assertEquals(
                URIUtil.htmlUriToLink(new URI("https://a/"), new URI("https://b/")),
                "../../../resources/b/index.html");
        assertEquals(
                URIUtil.htmlUriToLink(new URI("https://a/"), new URI("https://b/sub.html")),
                "../../../resources/b/sub.html");
        assertEquals(
                URIUtil.contentUriToLink(new URI("https://a/"), new URI("https://a/b/c/script.js")),
                "../../../resources/a/b/c/script.js");
        assertEquals(
                URIUtil.contentUriToLink(
                        new URI("https://a/b/index.html"), new URI("https://a/b/c/script.js")),
                "../../../../resources/a/b/c/script.js");
    }
}
