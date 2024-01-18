import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Optional;
import org.jsoup.nodes.Document;

public class IOUtil {
    public static Optional<Document> downloadHtml(URI uri) {
        return Optional.empty();
    }

    public static void downloadFile(URI uri, String path) {
    }

    public static void saveFile(String input, String path) {
        try (var outputStream = new PrintWriter(path)) {
            outputStream.print(input);
        } catch (IOException ex) {
            System.err.println("saveFile: " + ex.getMessage());
        }
    }

    public static void saveFile(InputStream input, String path) {
        try (var outputStream = new PrintWriter(path)) {
            outputStream.print(input);
        } catch (IOException ex) {
            System.err.println("saveFile: " + ex.getMessage());
        }
    }
}
