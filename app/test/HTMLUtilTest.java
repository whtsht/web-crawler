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
                                new URI("http://example.com/a/b/index.html"),
                                new URI("http://example.com/"));
                var uriList = HTMLUtil.replaceUri("src", URI::toString).apply(html);
                assertEquals(uriList, List.of(new URI("http://example.com/a/b/c/index.html")));
                assertEquals(html.document.body().select("[src]").get(0).attr("src"),
                                "http://example.com/a/b/c/index.html");
        }

        @Test
        void isHtml() throws URISyntaxException {
                assertTrue(HTMLUtil.isHtml(new URI("https://www.ikyu.com/?ikCo=ik010002&sc_e=ytc_pc_ikyu")));
                assertTrue(HTMLUtil.isHtml(new URI("https://www.yahoo.co.jp/")));
                assertTrue(HTMLUtil.isHtml(new URI("https://www.yahoo.co.jp")));
                assertTrue(HTMLUtil.isHtml(new URI("https://www.yahoo.co.jp/index.html")));
                assertTrue(HTMLUtil.isHtml(new URI(
                                "https://app.adjust.com/1kmi1m2?redirect=https%3A%2F%2Fpaypayfleamarket.yahoo.co.jp%2F%3Fcpt_s%3Dytopmedia%26cpt_m%3Dkotei%26cpt_n%3Dcontent%26cpt_c%3Dytopmedia")));
                assertFalse(HTMLUtil.isHtml(new URI("https://www.yahoo.co.jp/script.js")));
                assertFalse(HTMLUtil.isHtml(new URI("https://www.yahoo.co.jp/a/b/c/image.png")));
        }

        @Test
        void hyperLink() throws URISyntaxException {
                assertEquals(HTMLUtil.hyperLink(new URI("https://www.yahoo.co.jp/"))
                                .apply(new URI("https://example.com/")),
                                "../../resources/example.com/index.html");
                assertEquals(
                                HTMLUtil.hyperLink(new URI("https://www.yahoo.co.jp/"))
                                                .apply(new URI("https://example.com/index.html")),
                                "../../resources/example.com/index.html");
                assertEquals(
                                HTMLUtil.hyperLink(new URI("https://www.yahoo.co.jp/a/b/index.html"))
                                                .apply(new URI("https://s.yimg.jp/images/kaleido/edit/202310/1/3x3ucu1NQTyH9sCmYHnyTw.jpg")),
                                "../../../../resources/s.yimg.jp/images/kaleido/edit/202310/1/3x3ucu1NQTyH9sCmYHnyTw.jpg");
        }
}
