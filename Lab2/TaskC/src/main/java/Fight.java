import java.util.List;
import java.util.concurrent.RecursiveTask;

public class Fight extends RecursiveTask<Monk> {
    private final List<Monk> monks;

    Fight(List<Monk> monks) {
        this.monks = monks;
    }

    @Override
    protected Monk compute() {
        if (this.monks.size() == 1) {
            return this.monks.get(0);
        }

        int middle = monks.size() / 2;
        Fight leftFight = new Fight(this.monks.subList(0, middle));
        Fight rightFight = new Fight(this.monks.subList(middle, this.monks.size()));

        leftFight.fork();
        rightFight.fork();

        Monk leftFighter = leftFight.join();
        Monk rightFighter = rightFight.join();

        return leftFighter.fight(rightFighter) ? leftFighter : rightFighter;
    }
}
