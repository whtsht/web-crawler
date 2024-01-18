import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.Jsoup;

public class Crawler {
    private LinkedList<URI> urlList;

    public Crawler() throws Exception {
        var resources = new File("resources");
        resources.mkdir();

        urlList = new LinkedList<URI>();
    }

    private void saveFile(InputStream input, String destPath) throws IOException {
        try (var outputStream = new FileOutputStream(destPath)) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    public static String URINormalization(String host, String input) {
        if (input.length() >= 2 && input.substring(0, 2).equals("//")) {
            input = "https:" + input;
        } else if (input.length() >= 1 && input.substring(0, 1).equals("/")) {
            input = host + input;
        }

        return input;
    }

    private void crawl_step() {
        var path = urlList.pollFirst();
        var filedir = "file:///home/toma/Code/College/crawler/app/resources/";

        var client = HttpClient.newHttpClient();
        try {
            var req = HttpRequest.newBuilder().uri(path).GET().timeout(Duration.ofSeconds(5)).build();
            var res = client.send(req, HttpResponse.BodyHandlers.ofInputStream());
            var headers = res.headers().map();
            var isHTML = headers.get("content-type").get(0).contains("text/html");
            if (isHTML) {
                var saveFolder = new File("resources/" + path.getHost() + path.getPath());
                saveFolder.getParentFile().mkdirs();
                saveFolder.mkdir();

                var doc = Jsoup.parse(res.body(), null, path.getHost());

                var sources = doc.select("[src]");
                for (var source : sources) {
                    var url = source.attr("src");
                    url = URINormalization(path.getHost(), url);

                    var uri = URI.create(url);
                    var conv = filedir + uri.getHost() + uri.getRawPath();

                    source.attr("src", conv);
                    urlList.addLast(uri);
                }

                var links = doc.select("[href]");
                for (var link : links) {
                    var url = link.attr("href");
                    url = URINormalization(path.getHost(), url);
                    var uri = URI.create(url);
                    var conv = filedir + uri.getHost() + uri.getRawPath();
                    if (link.tagName().equals("a")) {
                        conv += "/index.html";
                    }

                    link.attr("href", conv);
                    urlList.addLast(uri);
                }

                System.out.println(path + " -> " + saveFolder + "/index.html");
                try (var pw = new PrintWriter(saveFolder + "/index.html")) {
                    pw.print(doc.outerHtml());
                }

            } else {
                System.out.println(path + " -> " + "resources/" + path.getHost() + path.getRawPath());
                var saveFolder = new File("resources/" + path.getHost() + path.getRawPath());
                saveFolder.getParentFile().mkdirs();

                try {
                    saveFile(res.body(), "resources/" + path.getHost() + path.getRawPath());
                } catch (Exception ex) {
                    // A non-html file was returned from the root directory
                    // ignore
                }
            }

        } catch (NullPointerException ex) {
            // content-type header not found
            // skip
        } catch (IllegalArgumentException ex) {
            // invalid url format
            // skip
        } catch (HttpTimeoutException ex) {
            // request timed out
            // skip
        } catch (ConnectException ex) {
            // failed to connect
            // skip
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    public void crawl(String url) {
        urlList.add(URI.create(url));

        while (!urlList.isEmpty()) {
            crawl_step();
        }
    }

    public static List<URI> crawlingOneStep(List<URI> uriList) {
        return uriList;
    }

    public static void crawlingWithDepth(int depth, URI uri) {
    }
}
