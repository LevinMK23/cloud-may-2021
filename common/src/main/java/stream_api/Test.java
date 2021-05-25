package stream_api;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Test {
    public static void main(String[] args) {

        Foo foo = x -> System.out.println(x + 1);

        Gen<String> o = System.out::println;
        o.call("12");

        Consumer<String> consumer = s -> {
            s = "Hello " + s;
            System.out.println(s);
        };

        consumer.accept("world");

        Predicate<Integer> predicate = x -> x % 2 == 0;
        System.out.println(predicate.test(5));

        Function<String, Integer> function = String::length;
        System.out.println(function.apply("6721"));

        Supplier<HashMap<String, Integer>> supplier = HashMap::new;
        supplier.get();

        System.out.println(foo.getClass());
    }
}
