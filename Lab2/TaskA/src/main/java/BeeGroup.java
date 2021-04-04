public class BeeGroup extends Thread {
    BeeManager manager;

    BeeGroup(BeeManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        while (true) {
            SearchTask task = this.manager.getTask();
            if (task == null) {
                return;
            }

            for (int i = task.getStartX(); i < task.getEndX(); i++) {
                for (int j = task.getStartY(); j < task.getEndY(); j++) {
                    try {
                        if (this.manager.getForest().isWinnieHere(i, j)) {
                            this.manager.setWinniCoordinates(i, j);
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
