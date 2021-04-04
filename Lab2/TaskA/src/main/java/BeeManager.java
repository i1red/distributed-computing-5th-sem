import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeeManager {
    private final Forest forest;
    private final List<BeeGroup> beeGroups;
    private volatile Queue<SearchTask> tasks;
    private volatile Integer winnieX, winnieY;

    public BeeManager(Forest forest, int forestSidesDivisor, int beeGroupsCount) {
        this.forest = forest;
        this.tasks = this.createTasks(forestSidesDivisor);
        this.beeGroups = Stream.generate(() -> new BeeGroup(this)).limit(beeGroupsCount).collect(Collectors.toList());
    }

    private Queue<SearchTask> createTasks(int forestSidesDivisor) {
        Queue<SearchTask> tasks = new LinkedList<SearchTask>();

        var zoneHeight = (int) Math.ceil((double)this.getForest().getHeight() / forestSidesDivisor);
        var zoneWidth = (int) Math.ceil((double)this.getForest().getWidth() / forestSidesDivisor);

        for (int i = 0; i < forestSidesDivisor; i++) {
            for (int j = 0; j < forestSidesDivisor; j++) {
                tasks.add(
                        new SearchTask(
                                zoneWidth * i, Math.max(zoneWidth * (i + 1), this.getForest().getWidth()),
                                zoneHeight * j, Math.max(zoneHeight * (j + 1), this.getForest().getHeight())
                        )
                );
            }
        }

        return tasks;
    }

    public Forest getForest() {
        return this.forest;
    }

    public synchronized SearchTask getTask() {
        if (this.tasks.size() == 0 || this.winnieX != null) {
            for (BeeGroup beeGroup: this.beeGroups) {
                beeGroup.interrupt();
            }
            return null;
        }

        return this.tasks.poll();
    }

    public void searchForWinni() throws InterruptedException {
        for (BeeGroup beeGroup: this.beeGroups) {
            beeGroup.start();
        }

        for (BeeGroup beeGroup: this.beeGroups) {
            beeGroup.join();
        }
    }

    public void setWinniCoordinates(int x, int y) {
        this.winnieX = x;
        this.winnieY = y;
    }

    public Integer getWinnieX() {
        return this.winnieX;
    }

    public Integer getWinnieY() {
        return this.winnieY;
    }
}
