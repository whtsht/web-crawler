import java.io.File;
import java.net.URI;

public class App {
    public static final void main(String[] args) throws Exception {
        IOUtil.deleteDirectory(new File("resources"));
        Crawler.crawlingWithDepth(1, new URI("https://www.yahoo.co.jp/"));
        // URI uri = new
        // URI("https://www.ikyu.com/ikCo.ashx?cosid=ik010002&surl=%2F&sc_e=ytc_pc_ikyu");

        // app/resources/www.ikyu.com/ikCo.ashx?cosid=ik010002&surl=%2F&sc_e=ytc_pc_ikyu/index.html
    }
}
