import java.util.Arrays;

public class App {
    public static final void main(String[] args) throws Exception {
        final var list = Arrays.asList(1, 2, 3, 4);
        var stream = list.stream();
        for (int i = 0; i < 3; i++) {
            stream = stream.map(a -> a + 1);
        }
        System.out.println(stream.toList());
    }
}
