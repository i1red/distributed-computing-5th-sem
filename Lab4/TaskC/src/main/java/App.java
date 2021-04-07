import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class App {
    private static final String KYIV = "Kyiv";
    private static final String LVIV = "Lviv";
    private static final String KHERSON = "Kherson";
    private static final String KHARKIV = "Kharkiv";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        var schedule = new BusSchedule();
        var threadCreator = new ThreadCreator(new ReentrantReadWriteLock(false), schedule);

        FutureTask<Void> addKyivTask = threadCreator.addBusStop(KYIV);
        FutureTask<Void> addLvivTask = threadCreator.addBusStop(LVIV);
        FutureTask<Void> addKhersonTask = threadCreator.addBusStop(KHERSON);
        FutureTask<Void> addKharkivTask = threadCreator.addBusStop(KHARKIV);

        addKharkivTask.get();
        addKyivTask.get();
        addLvivTask.get();
        addKhersonTask.get();

        System.out.println();

        FutureTask<Void> addTripKyivLviv = threadCreator.addTrip(KYIV, LVIV, 150);
        FutureTask<Void> addTripKhersonLviv = threadCreator.addTrip(KHERSON, LVIV, 170);
        FutureTask<Void> addTripKhersonKharkiv = threadCreator.addTrip(KHERSON, KHARKIV, 120);
        FutureTask<Void> addTripKhersonKyiv = threadCreator.addTrip(KHERSON, KYIV, 100);

        addTripKhersonKharkiv.get();
        addTripKhersonLviv.get();
        addTripKyivLviv.get();
        addTripKhersonKyiv.get();

        System.out.println();

        threadCreator.changeTripPrice(KYIV, KHERSON, 80).get();

        System.out.println();

        System.out.printf("Price for trip %s - %s: %d\n", LVIV, KHARKIV, threadCreator.getTripPrice(LVIV, KHARKIV).get());
        System.out.printf("Price for trip %s - %s: %d\n", KHERSON, KHERSON, threadCreator.getTripPrice(KHERSON, KHERSON).get());
        System.out.printf("Price for trip %s - %s: %d\n", KYIV, KHERSON, threadCreator.getTripPrice(KYIV, KHERSON).get());
        System.out.printf("Price for trip %s - %s: %d\n", KHARKIV, KYIV, threadCreator.getTripPrice(KHARKIV, KYIV).get());

        System.out.println();

        threadCreator.deleteTrip(KHERSON, KYIV).get();

        System.out.println();

        System.out.printf("Price for flight %s - %s: %d\n", KYIV, KHERSON, threadCreator.getTripPrice(KYIV, KHERSON).get());

        System.out.println();

        threadCreator.deleteBusStop(LVIV).get();

        System.out.println();

        System.out.printf("Price for trip %s - %s: %d\n", KHARKIV, KYIV, threadCreator.getTripPrice(KHARKIV, KYIV).get());
    }
}
