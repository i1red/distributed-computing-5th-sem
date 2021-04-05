public class CustomSemaphore {
    private volatile boolean isTaken = false;

    synchronized void get() {
        while (this.isTaken) {
            try {
                this.wait();
            } catch (InterruptedException ignored) {}
        }
        this.isTaken = true;
    }

    synchronized void release() {
        if (this.isTaken) {
            this.isTaken = false;
            this.notify();
        }
    }
}
