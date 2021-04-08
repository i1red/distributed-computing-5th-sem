import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;

public class App {
    private static final int STRING_COUNT = 4;

    private static final ArrayList<StringBuilder> strings = new ArrayList<>(
            Arrays.asList(
                    new StringBuilder("ABABBCBDBDBDBCB"),
                    new StringBuilder("ADADADADADADDDD"),
                    new StringBuilder("BCBCBCBCCCBCBCBCB"),
                    new StringBuilder("ABCDBCBDCBBCDBCABCD")
            )
    );
    private static final ArrayList<Thread> threads = new ArrayList<>();

    static ArrayList<StringBuilder> getStrings() {
        return strings;
    }

    static ArrayList<Thread> getThreads() {
        return threads;
    }

    public static void main(String[] args) throws InterruptedException {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new EqualQuantityCheck());

        for (int i = 0; i < STRING_COUNT; i++) {
            threads.add(new Thread(new StringChanger(i, cyclicBarrier)));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

}
