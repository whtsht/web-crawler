import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URISyntaxException;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import io.vavr.collection.List;

class HTMLUtilTest {
    @Test
    void replaceSrc() throws URISyntaxException {
        var html = new HTML(Jsoup.parse("<a src=\"/a/b/c/index.html\"></a>"),
                new URI("http://example.com/"));
        var uriList = HTMLUtil.replaceUri("src", e -> e).apply(html);
        assertEquals(uriList, List.of(new URI("http://example.com/a/b/c/index.html")));
        assertEquals(html.document.body().select("[src]").get(0).attr("src"), "http://example.com/a/b/c/index.html");
    }
}
