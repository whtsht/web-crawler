import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class IOUtilTest {
    @Test
    void downloadValidUri() {
        Optional<HttpResponse<InputStream>> response;
        response = IOUtil.downloadContent("https://www.yahoo.co.jp/");
        assertEquals(response.isPresent(), true);

        response = IOUtil.downloadContent("https://www.youtube.com/");
        assertEquals(response.isPresent(), true);

        response = IOUtil.downloadContent("https://github.com");
        assertEquals(response.isPresent(), true);
    }

    @Test
    void downloadInvalidUri() {
        Optional<HttpResponse<InputStream>> response;
        response = IOUtil.downloadContent("htts://www.yahoo.co.jp/");
        assertEquals(response.isPresent(), false);

        response = IOUtil.downloadContent("https://unknown.unknown");
        assertEquals(response.isPresent(), false);

        response = IOUtil.downloadContent("");
        assertEquals(response.isPresent(), false);
    }
}
