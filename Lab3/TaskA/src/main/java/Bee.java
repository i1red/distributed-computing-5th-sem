public class Bee extends Thread {
    private final int beeId;
    private final HoneyPot honeyPot;
    private final Bear bear;
    private final CustomSemaphore semaphore;

    Bee(int beeId, HoneyPot honeyPot, Bear bear, CustomSemaphore semaphore) {
        this.beeId = beeId;
        this.honeyPot = honeyPot;
        this.bear = bear;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        while (true) {
            this.semaphore.get();
            if (this.honeyPot.isAlreadyEaten()) {
                this.semaphore.release();
                return;
            }

            if (!this.honeyPot.isFull()) {
                System.out.printf("Bee %d is collecting honey...\n", this.beeId);
                this.honeyPot.addPortion();
                System.out.printf("Bee %d has added portion to the pot...\n", this.beeId);
            }

            if (this.honeyPot.isFull()) {
                try {
                    System.out.printf("Bee %d has waken the bear...\n", this.beeId);
                    this.bear.consumeHoney(this.honeyPot);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.semaphore.release();
                return;
            }

            this.semaphore.release();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
