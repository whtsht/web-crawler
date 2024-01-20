import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

class CrawlerTest {
    public static void deleteDirectory(File directory) {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            }
            file.delete();
        }
        directory.delete();
    }

    @AfterAll
    static void cleanUp() throws IOException {
        deleteDirectory(new File("resources"));
    }

    @Test
    void pullContents() throws URISyntaxException {
        var uri = new URI("https://www.yahoo.co.jp/");
        var response = IOUtil.downloadContent(uri).get();
        var html = Crawler.pullContents(response, uri, URIUtil.getBaseUri(uri).get());
        assertTrue(html.isDefined());

        uri = new URI("https://www.w3schools.com/tags/img_girl.jpg");
        response = IOUtil.downloadContent(uri).get();
        html = Crawler.pullContents(response, uri, URIUtil.getBaseUri(uri).get());
        assertTrue(html.isEmpty());
        var file = new File("resources/www.w3schools.com/tags/img_girl.jpg");
        assertTrue(file.exists());
    }
}
