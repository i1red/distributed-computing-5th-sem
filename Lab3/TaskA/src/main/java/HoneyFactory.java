import java.util.ArrayList;
import java.util.List;

public class HoneyFactory {
    private final Bear bear;
    private final List<Bee> bees;
    private final HoneyPot honeyPot;

    public HoneyFactory(Bear bear, int numberOfBees, int capacityOfHoneyPot) {
        this.bear = bear;
        this.honeyPot = new HoneyPot(capacityOfHoneyPot);
        this.bees = this.initializeBees(numberOfBees);
    }

    private List<Bee> initializeBees(int numberOfBees) {
        List<Bee> bees = new ArrayList<>(numberOfBees);
        CustomSemaphore semaphore = new CustomSemaphore();

        for (int i = 1; i <= numberOfBees; i++) {
            bees.add(new Bee(i, this.honeyPot, this.bear, semaphore));
        }

        return bees;
    }

    public void startFactory() throws InterruptedException {
        this.honeyPot.emptyThePot();
        for (Bee bee : this.bees) {
            bee.start();
        }

        for (Bee bee: this.bees){
            bee.join();
        }
        //while (!honeyPot.isAlreadyEaten());
    }
}
