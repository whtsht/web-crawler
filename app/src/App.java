import io.vavr.collection.List;
import io.vavr.control.Try;

public class App {
    public static final void main(String[] args) throws Exception {
        var a = List.of(Try.of(() -> af()), Try.of(() -> a()), Try.of(() -> a()));
        var am = a.filter(e -> e.isFailure()).map(e -> e);
        System.out.println(am);
    }

    private static String a() {
        return "a";
    }

    private static String af() {
        throw new RuntimeException("a");
    }
}
