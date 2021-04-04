import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        FistPathCompetition competition = new FistPathCompetition(
                Arrays.asList(12, 19, 15, 28, 3, 1, 18),
                Arrays.asList(7, 16, 28, 27, 20, 20, 13, 5, 10, 2)
        );

        System.out.printf("%s monastery won\n", competition.getCompetitionWinner());
    }
}
