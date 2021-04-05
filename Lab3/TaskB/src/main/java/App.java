public class App {
    public static void main(String[] args) {
        Barber barber = new Barber();
        for (int i = 1; i <= 15; i++) {
            new Visitor(i, barber).start();
        }
    }
}
