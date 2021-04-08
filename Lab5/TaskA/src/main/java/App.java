import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws InterruptedException {
        Direction[] soldiers = Utils.generateDirections(Const.SOLDIERS_COUNT);

        var barrier = new CustomCyclicBarrier(Const.GROUPS_NUMBER);
        int partSize = (int) Math.ceil((double)Const.SOLDIERS_COUNT / Const.GROUPS_NUMBER);
        var threads = new ArrayList<Thread>();

        for (int i = 0; i < Const.GROUPS_NUMBER; i++) {
            int startIndex = i * partSize, endIndex = Math.min((i + 1) * partSize, Const.SOLDIERS_COUNT);
            threads.add(new Thread(
                    new SoldiersGroup(soldiers, startIndex, endIndex, barrier)
            ));
        }

        for (Thread thread: threads) {
            thread.start();
        }

        for (Thread thread: threads) {
            thread.join();
        }
    }
}
