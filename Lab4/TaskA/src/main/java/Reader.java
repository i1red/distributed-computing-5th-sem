import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.FutureTask;

public class Reader {
    private final String filepath;
    private final CustomReadWriteLock lock;

    public Reader(String filepath, CustomReadWriteLock lock) {
        this.filepath = filepath;
        this.lock = lock;
    }

    public FutureTask<String> findNameByNumber(String number) {
        FutureTask<String> futureTask = getFutureTask(number, (key, name, number_) -> key.equals(number_) ? name : null);
        new Thread(futureTask).start();

        return futureTask;
    }

    public FutureTask<String> findNumberByName(String name) {
        FutureTask<String> futureTask = getFutureTask(name, (key, name_, number) -> key.equals(name_) ? number : null);
        new Thread(futureTask).start();

        return futureTask;
    }

    private FutureTask<String> getFutureTask(String searchKey, TernaryFunction<String, String, String, String> predicate) {
        return new FutureTask<>(
                () -> {
                    this.lock.readLock();

                    try (BufferedReader reader = new BufferedReader(new FileReader(this.filepath))){
                        for (String recordString = reader.readLine(); recordString != null; recordString = reader.readLine()) {
                            String[] values = recordString.split(Const.VALUE_SEPARATOR);
                            String result = predicate.apply(searchKey, values[0], values[1]);

                            if (result != null) {
                                this.lock.readUnlock();
                                return result;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    this.lock.readUnlock();
                    return null;
                }
        );

    }
}
