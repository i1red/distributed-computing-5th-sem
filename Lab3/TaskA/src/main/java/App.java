public class App {
    public static void main(String[] args) throws InterruptedException {
        Bear WinniThePooh = new Bear();
        HoneyFactory factory = new HoneyFactory(WinniThePooh, 4, 50);
        factory.startFactory();
    }
}
