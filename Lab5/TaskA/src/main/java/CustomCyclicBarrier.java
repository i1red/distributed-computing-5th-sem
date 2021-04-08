import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;

public class CustomCyclicBarrier {
    private final int parties;
    private final Runnable barrierEvent;
    private boolean isBroken;
    private final Set<Thread> awaitingThreads;


    public CustomCyclicBarrier(int parties, Runnable barrierEvent) {
        this.parties = parties;
        this.barrierEvent = barrierEvent;
        this.isBroken = false;
        this.awaitingThreads = new HashSet<>();
    }

    public CustomCyclicBarrier(int parties) {
        this(parties, null);
    }

    public synchronized void await() throws BrokenBarrierException {
        if (this.isBroken()) {
            throw new BrokenBarrierException();
        }

        this.awaitingThreads.add(Thread.currentThread());

        while (this.awaitingThreads.size() != this.parties && this.awaitingThreads.contains(Thread.currentThread())) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                this.isBroken = true;
                throw new BrokenBarrierException(e.getMessage());
            }
        }

        this.reset();
        this.notifyAll();

        if (this.barrierEvent != null) {
            this.barrierEvent.run();
        }

    }

    public void reset() {
        this.awaitingThreads.clear();
        this.isBroken = false;
    }

    public boolean isBroken() {
        return this.isBroken;
    }

    public int getParties() {
        return this.parties;
    }
}
