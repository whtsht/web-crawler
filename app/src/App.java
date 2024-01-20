import java.net.URI;;

public class App {
    public static final void main(String[] args) throws Exception {
        Crawler.crawlingWithDepth(1, new URI("https://stackoverflow.com/"));
    }
}
