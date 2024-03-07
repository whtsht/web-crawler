import java.io.File;
import java.net.URI;

public class App {
    public static final void main(String[] args) throws Exception {
        IOUtil.deleteDirectory(new File("resources"));
        Crawler.crawlingWithDepth(2, new URI("https://www.yahoo.co.jp/"));
    }
}
