import java.util.Random;

public class Monk {
    private final Monastery monastery;
    private final int qiEnergy;

    public Monk(Monastery monastery, int qiEnergy) {
        this.monastery = monastery;
        this.qiEnergy = qiEnergy;
    }

    public Monastery getMonastery() {
        return this.monastery;
    }

    public boolean fight(Monk other) {
        if (this.qiEnergy > other.qiEnergy) {
            return true;
        }

        if (this.qiEnergy < other.qiEnergy) {
            return false;
        }

        return new Random().nextBoolean();
    }
}
