import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class URIUtilTest {
    @Test
    void success() throws URISyntaxException {
        var uri = new URI("https://stackoverflow.com/jobs/companies");

        var baseUri = URIUtil.getBaseUri(uri);
        assertEquals(baseUri, Optional.of(new URI("https://stackoverflow.com/")));

        Optional<URI> absoluteUri;
        absoluteUri = URIUtil.uriNormalization(baseUri.get(), new URI("/users"));
        assertEquals(absoluteUri, Optional.of(new URI("https://stackoverflow.com/users")));

        absoluteUri = URIUtil.uriNormalization(baseUri.get(), new URI("/"));
        assertEquals(absoluteUri, Optional.of(new URI("https://stackoverflow.com/")));

        absoluteUri = URIUtil.uriNormalization(baseUri.get(), new URI("//stackoverflow.com/"));
        assertEquals(absoluteUri, Optional.of(new URI("https://stackoverflow.com/")));
    }
}
