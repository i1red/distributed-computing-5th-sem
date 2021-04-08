import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    public static void printDirections(Direction[] directions) {
        System.out.println(
                Arrays.stream(directions).map(e -> e == Direction.LEFT ? "ᐊ" : "ᐅ")
                        .collect(Collectors.joining(""))
        );
    }

    public static <T> ArrayList<T> listOf(Supplier<T> supplier, int size) {
        return Stream.generate(supplier).limit(size).collect(Collectors.toCollection(ArrayList::new));

    }

    public static Direction[] generateDirections(int size) {
        var random = new Random();
        return Stream.generate(() -> Direction.values()[random.nextInt( 2)])
                .limit(size).toArray(Direction[]::new);
    }
}
