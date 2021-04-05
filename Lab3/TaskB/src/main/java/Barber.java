import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Barber {
    private final Semaphore semaphore = new Semaphore(1);
    private volatile Queue<Visitor> queue = new LinkedList<>();

    public void doHaircut(Visitor visitor) {
        try {
            semaphore.acquire();
            queue.offer(visitor);
            doHaircutImpl(queue.remove());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        semaphore.release();
    }

    private void doHaircutImpl(Visitor visitor) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {}
        System.out.printf("Barber has done a haircut for visitor %d\n", visitor.getVisitorId());
    }
}
