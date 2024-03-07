import java.io.File;
import java.net.URI;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;

import io.vavr.collection.List;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class App {
    public static final void main(String[] args) throws Exception {
        IOUtil.deleteDirectory(new File("resources"));

        final var inputs = new ArrayList<ArrayBlockingQueue<URI>>();
        final var outputs = new ArrayList<ArrayBlockingQueue<URI>>();
        // TODO nThread = number of logic core
        final var nWorker = 100;
        final var executor = Executors.newFixedThreadPool(nWorker);
        final var queueSize = 1000;

        for (var i = 0; i < nWorker; i++) {
            final var input = new ArrayBlockingQueue<URI>(queueSize);
            final var output = new ArrayBlockingQueue<URI>(queueSize);
            executor.execute(() -> {
                worker(output, input);
            });
            inputs.add(input);
            outputs.add(output);
        }
        var uri = new URI("https://www.yahoo.co.jp/");
        outputs.get(0).put(uri);

        master(inputs, outputs, nWorker, uri);
    }

    public static void master(ArrayList<ArrayBlockingQueue<URI>> inputs,
            ArrayList<ArrayBlockingQueue<URI>> outputs, int nWorker, URI uri) throws Exception {
        final var reachedUris = new HashSet<URI>(Arrays.asList(uri));
        var nextUris = new HashSet<URI>();
        for (;;) {
            Thread.sleep(1000);

            for (int i = 0; i < nWorker; i++) {
                inputs.get(i).drainTo(nextUris);
            }

            var tmp = new HashSet<>(nextUris.stream().filter(u -> !reachedUris.contains(u)).toList());
            reachedUris.addAll(nextUris);

            var i = 0;
            for (var t : tmp) {
                if (!outputs.get(i % nWorker).offer(t)) {
                    nextUris.add(t);
                }
            }
        }
    }

    public static void worker(ArrayBlockingQueue<URI> input, ArrayBlockingQueue<URI> output) {
        var nextUris = new HashSet<URI>();
        for (;;) {
            try {
                final var uri = input.take();
                final var newUris = Crawler.crawlingOneStep(List.of(uri)).toJavaSet();
                newUris.addAll(nextUris);
                nextUris.clear();

                for (var u : newUris) {
                    if (!output.offer(u)) {
                        nextUris.add(u);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
