public class HoneyPot {
    private volatile int currentOccupancy = 0;
    private volatile boolean alreadyEaten = false;
    private final int capacity;

    HoneyPot(int capacity) {
        this.capacity = capacity;
    }

    void addPortion() {
        if (!this.isFull()) {
            currentOccupancy = currentOccupancy + 1;
        }
    }

    void eat() throws Exception {
        if (isFull()) {
            currentOccupancy = 0;
            alreadyEaten = true;
        } else {
            throw new Exception("Honey pot not full!");
        }
    }

    public boolean isFull() {
        return (currentOccupancy == capacity);
    }

    void emptyThePot() {
        this.currentOccupancy = 0;
        this.alreadyEaten = false;
    }

    public boolean isAlreadyEaten() {
        return alreadyEaten;
    }

    public int getCapacity() {
        return capacity;
    }
}
