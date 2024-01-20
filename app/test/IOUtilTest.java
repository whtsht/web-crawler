import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;

import io.vavr.control.Try;

class IOUtilTest {
    @Test
    void downloadValidUri() {
        Try<HttpResponse<InputStream>> response;
        response = IOUtil.downloadContent("https://www.yahoo.co.jp/");
        assertTrue(response.isSuccess());

        response = IOUtil.downloadContent("https://www.youtube.com/");
        assertTrue(response.isSuccess());

        response = IOUtil.downloadContent("https://github.com");
        assertTrue(response.isSuccess());
    }

    @Test
    void downloadInvalidUri() {
        Try<HttpResponse<InputStream>> response;
        response = IOUtil.downloadContent("htts://www.yahoo.co.jp/");
        assertTrue(response.isFailure());

        response = IOUtil.downloadContent("https://unknown.unknown");
        assertTrue(response.isFailure());

        response = IOUtil.downloadContent("");
        assertTrue(response.isFailure());
    }
}
