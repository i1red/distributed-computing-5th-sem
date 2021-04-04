import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FistPathCompetition {
    private final List<Monk> monks;

    public FistPathCompetition(List<Integer> guanYinEnergies, List<Integer> guanYangEnergies) {
        this.monks = Stream.concat(
                guanYinEnergies.stream().map(energy -> new Monk(Monastery.GUAN_YIN, energy)),
                guanYangEnergies.stream().map(energy -> new Monk(Monastery.GUAN_YANG, energy))
        ).collect(Collectors.toList());
        Collections.shuffle(this.monks);
    }

    public Monastery getCompetitionWinner() {
        Monk winner = ForkJoinPool.commonPool().invoke(new Fight(monks));
        return winner.getMonastery();
    }
}
