import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import io.vavr.control.Try;

class IOUtilTest {
    @Test
    void downloadValidUri() throws URISyntaxException {
        Try<HttpResponse<InputStream>> response;
        response = IOUtil.downloadContent(new URI("https://www.yahoo.co.jp/"));
        assertTrue(response.isSuccess());

        response = IOUtil.downloadContent(new URI("https://www.youtube.com/"));
        assertTrue(response.isSuccess());

        response = IOUtil.downloadContent(new URI("https://github.com"));
        assertTrue(response.isSuccess());
    }

    @Test
    void downloadInvalidUri() throws URISyntaxException {
        Try<HttpResponse<InputStream>> response;
        response = IOUtil.downloadContent(new URI("htts://www.yahoo.co.jp/"));
        assertTrue(response.isFailure());

        response = IOUtil.downloadContent(new URI("https://unknown.unknown"));
        assertTrue(response.isFailure());
    }
}
