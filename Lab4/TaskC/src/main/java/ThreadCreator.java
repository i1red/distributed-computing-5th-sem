import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.ReadWriteLock;

public class ThreadCreator {
    private final ReadWriteLock lock;
    private final BusSchedule schedule;

    public ThreadCreator(ReadWriteLock lock, BusSchedule schedule) {
        this.lock = lock;
        this.schedule = schedule;
    }

    public FutureTask<Void> addBusStop(String city) {
        var futureTask = new FutureTask<Void>(() -> {
            lock.writeLock().lock();
            schedule.addBusStop(city);
            lock.writeLock().unlock();
            return null;
        });

        new Thread(futureTask).start();
        return futureTask;
    }

    public FutureTask<Void> deleteBusStop(String city) {
        var futureTask = new FutureTask<Void>(() -> {
            lock.writeLock().lock();
            schedule.deleteBusStop(city);
            lock.writeLock().unlock();
            return null;
        });

        new Thread(futureTask).start();
        return futureTask;
    }

    public FutureTask<Void> changeTripPrice(String firstCity, String secondCity, int price) {
        var futureTask = new FutureTask<Void>(() -> {
            lock.writeLock().lock();
            schedule.changeTripPrice(firstCity, secondCity, price);
            lock.writeLock().unlock();
            return null;
        });

        new Thread(futureTask).start();
        return futureTask;
    }

    public FutureTask<Void> addTrip(String firstCity, String secondCity, int price) {
        var futureTask = new FutureTask<Void>(() -> {
            lock.writeLock().lock();
            schedule.addTrip(firstCity, secondCity, price);
            lock.writeLock().unlock();
            return null;
        });

        new Thread(futureTask).start();
        return futureTask;
    }

    public FutureTask<Void> deleteTrip(String firstCity, String secondCity) {
        var futureTask = new FutureTask<Void>(() -> {
            lock.writeLock().lock();
            schedule.deleteTrip(firstCity, secondCity);
            lock.writeLock().unlock();
            return null;
        });

        new Thread(futureTask).start();
        return futureTask;
    }

    public FutureTask<Integer> getTripPrice(String firstCity, String secondCity)  {
        var futureTask = new FutureTask<Integer>(() -> {
            lock.readLock().lock();
            Integer price = schedule.getTripPrice(firstCity, secondCity);
            lock.readLock().unlock();
            return price;
        });

        new Thread(futureTask).start();
        return futureTask;
    }


}
