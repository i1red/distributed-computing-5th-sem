public class App {
    public static void main(String[] args) throws Exception {
        var forest = new Forest(100, 100, 1, 99);

        BeeManager manager = new BeeManager(forest,9, 3);
        manager.searchForWinni();

        System.out.printf("Winnie is here: x=%d, y=%d\n", manager.getWinnieX(), manager.getWinnieY());
    }
}
