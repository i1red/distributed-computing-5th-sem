import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicBoolean;

public class SoldiersGroup implements Runnable {
    private static final List<SoldiersGroup> groups = new ArrayList<>();
    private static final List<Boolean> hasGroupCompleted = new ArrayList<>();
    private static final AtomicBoolean isCompleted = new AtomicBoolean(false);

    private final int groupId;
    private final Direction[] soldiersDirections;
    private final CustomCyclicBarrier barrier;
    private int startIndex;
    private int endIndex;

    SoldiersGroup(Direction[] soldiersDirections, int startIndex, int endIndex, CustomCyclicBarrier barrier) {
        this.soldiersDirections = soldiersDirections;
        this.groupId = groups.size();
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.barrier = barrier;

        groups.add(this);
        hasGroupCompleted.add(false);
    }

    private int orderSoldiersAndGetNewStartIndex() {
        int newStartIndex = this.startIndex;

        boolean areSoldiersOrdered;
        do {
            areSoldiersOrdered = true;

            boolean flag = true;

            for (int i = this.startIndex; i < this.endIndex - 1; i++) {
                if (this.soldiersDirections[i] == Direction.RIGHT ) {
                    if (flag) {
                        newStartIndex = i;
                    }
                    flag = false;
                }

                if (this.soldiersDirections[i] == Direction.RIGHT && this.soldiersDirections[i + 1] == Direction.LEFT) {
                    this.soldiersDirections[i] = Direction.LEFT;
                    this.soldiersDirections[i + 1] = Direction.RIGHT;
                    areSoldiersOrdered = false;

                    Utils.printDirections(this.soldiersDirections);
                }
            }

        } while (!areSoldiersOrdered);

        return newStartIndex;
    }


    private int getNewEndIndex() {
        return this.groupId != groups.size() - 1 ? groups.get(this.groupId + 1).startIndex : this.endIndex;
    }

    @Override
    public void run() {
        while (!isCompleted.get()) {
            if (!hasGroupCompleted.get(this.groupId)) {
                this.startIndex = this.orderSoldiersAndGetNewStartIndex();
            }

            try {
                this.barrier.await();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            synchronized (this.barrier) {
                int newEndIndex = this.getNewEndIndex();
                hasGroupCompleted.set(this.groupId, this.endIndex == newEndIndex);
                this.endIndex = newEndIndex;
            }

            if (!hasGroupCompleted.contains(false)) {
                isCompleted.set(true);
            }

        }
    }
}
