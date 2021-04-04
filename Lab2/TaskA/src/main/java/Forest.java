public class Forest {
    private final int width, height;
    private final int winnieX, winnieY;

    public Forest(int width, int height, int winnieX, int winnieY) throws Exception {
        if (width <= 0 || height <= 0) {
            throw new Exception("Width and height should be > 0");
        }

        if (winnieX < 0 || winnieY < 0 || winnieX >= width || winnieY >= height) {
            throw new Exception("Winnie is out of forest");
        }

        this.width = width;
        this.height = height;
        this.winnieX = winnieX;
        this.winnieY = winnieY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Boolean isWinnieHere(int x, int y) throws Exception {
        if (x < 0 || y < 0 || x >= this.getWidth() || y >= this.getHeight()) {
            throw new Exception("Out of forest");
        }

        return x == winnieX && y == winnieY;
    }
}
