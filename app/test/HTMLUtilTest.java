import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

class HTMLUtilTest {
    @Test
    void extractSrc() {
        Document doc;
        List<LinkedElement> linkedElementList;
        doc = Jsoup.parse("<img src=\"hello\"></img>");
        linkedElementList = HTMLUtil.extractSrc(doc).toList();
        assertEquals(linkedElementList.size(), 1);

        doc = Jsoup.parse("<h1>hello</h1>");
        linkedElementList = HTMLUtil.extractSrc(doc).toList();
        assertEquals(linkedElementList.size(), 0);
    }

    @Test
    void extractHerf() {
        Document doc;
        List<LinkedElement> linkedElementList;
        doc = Jsoup.parse("<a href=\"hello\"></a>");
        linkedElementList = HTMLUtil.extractHref(doc).toList();
        assertEquals(linkedElementList.size(), 1);

        doc = Jsoup.parse("<h1>hello</h1>");
        linkedElementList = HTMLUtil.extractHref(doc).toList();
        assertEquals(linkedElementList.size(), 0);
    }
}
