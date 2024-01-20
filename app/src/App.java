import java.net.URI;
import java.util.Arrays;

import org.jsoup.Jsoup;

import io.vavr.collection.List;

public class App {
    public static final void main(String[] args) throws Exception {

        var html = new HTML(Jsoup.parse("<h1>extract src attribute example</h1><a src=\"/a/b/c/index.html\"></a>"),
                new URI("http://example.com/"));
        var uriList = HTMLUtil.replaceUri("src", e -> e).apply(html);
        System.out.println(uriList);
        System.out.println(html.document);

    }
}
