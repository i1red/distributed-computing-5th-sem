class SearchTask {
    private final int startX, endX;
    private final int startY, endY;

    public SearchTask(int startX, int endX, int startY, int endY) {
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
    }

    public int getStartX() {
        return this.startX;
    }

    public int getEndX() {
        return this.endX;
    }

    public int getStartY() {
        return this.startY;
    }

    public int getEndY() {
        return this.endY;
    }
}
