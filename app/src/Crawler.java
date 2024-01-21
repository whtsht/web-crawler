import java.net.URI;

import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.function.Function;
import java.util.HashSet;
import java.util.Arrays;

public class Crawler {
    public static Option<Either<HTML, IOUtil.Content>> pullContents(HttpResponse<InputStream> response, URI uri,
            URI baseUri) {
        if (HTMLUtil.isHtml(response)) {
            return HTMLUtil.parseHtml(response, uri, baseUri).toOption().map(Either::left);
        } else {
            return Option.some(Either.right(new IOUtil.Content(response.body(), URIUtil.contentUriToFilename(uri))));
        }
    }

    public static List<URI> replaceUri(HTML html) {
        final Function<String, List<URI>> replaceUriWithAttr = attributeName -> HTMLUtil
                .replaceUri(attributeName, HTMLUtil.hyperLink(html.srcUri)).apply(html);

        return List.of("src", "href").flatMap(replaceUriWithAttr);
    }

    public static void saveDocument(HTML html) {
        IOUtil.saveFile(IOUtil.Content.of(html.document.outerHtml(),
                URIUtil.htmlUriToFilename(html.srcUri)));
    }

    private static <L, R> List<L> getLeft(List<Either<L, R>> list) {
        return list.filter(Either::isLeft).map(elem -> elem.left().get());
    }

    private static <L, R> List<R> getRight(List<Either<L, R>> list) {
        return list.filter(Either::isRight).map(elem -> elem.right().get());
    }

    public static List<URI> crawlingOneStep(List<URI> uriList) {
        final var contents = uriList.flatMap(
                uri -> IOUtil.downloadContent(uri).flatMap(
                        response -> URIUtil.getBaseUri(uri).map(
                                baseUri -> pullContents(response, uri, baseUri))))
                .flatMap(Option::toStream);

        List<HTML> htmlList = getLeft(contents);
        List<IOUtil.Content> contentList = getRight(contents);

        contentList.forEach(IOUtil::saveFile);

        final var newUriList = htmlList.flatMap(Crawler::replaceUri);
        htmlList.forEach(Crawler::saveDocument);
        return newUriList;
    }

    public static void crawlingOneStepWithoutNext(List<URI> uriList) {
        final var contents = uriList.flatMap(
                uri -> IOUtil.downloadContent(uri).flatMap(
                        response -> URIUtil.getBaseUri(uri).map(
                                baseUri -> pullContents(response, uri, baseUri))))
                .flatMap(Option::toStream);

        List<HTML> htmlList = getLeft(contents);
        List<IOUtil.Content> contentList = getRight(contents);

        contentList.forEach(IOUtil::saveFile);
        htmlList.forEach(Crawler::saveDocument);
    }

    public static void crawlingWithDepth(int depth, URI uri) {
        uri = URIUtil.normalize(uri).get();
        var uriList = List.of(uri);
        final var uriSet = new HashSet<URI>(Arrays.asList(uri));
        uriSet.add(uri);

        for (int i = 0; i < depth; i++) {
            final var newUriList = crawlingOneStep(uriList)
                    .map(URIUtil::normalize).flatMap(Function.identity())
                    .filter(uri_ -> !uriSet.contains(uri_));
            uriSet.addAll(newUriList.toJavaList());
            uriList = newUriList;
        }
        crawlingOneStepWithoutNext(uriList);
    }
}
