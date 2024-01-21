import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

class CrawlerTest {
    @Test
    void pullContents() throws URISyntaxException {
        var uri = new URI("https://www.yahoo.co.jp/");
        var response = IOUtil.downloadContent(uri).get();
        var html = Crawler.pullContents(response, uri, URIUtil.getBaseUri(uri).get());
        assertTrue(html.get().isLeft());

        uri = new URI("https://www.w3schools.com/tags/img_girl.jpg");
        response = IOUtil.downloadContent(uri).get();
        html = Crawler.pullContents(response, uri, URIUtil.getBaseUri(uri).get());
        assertTrue(html.get().isRight());
    }
}
