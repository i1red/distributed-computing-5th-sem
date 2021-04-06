import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.FutureTask;

public class Writer {
    private final String filepath;
    private final CustomReadWriteLock lock;


    public Writer(String filepath, CustomReadWriteLock lock) {
        this.filepath = filepath;
        this.lock = lock;
    }

    public FutureTask<Void> add(String name, String number) {
        FutureTask<Void> futureTask = new FutureTask<> (
                () -> {
                    this.lock.writeLock();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filepath), true))){
                        writer.write(String.format("%s%s%s%s",name, Const.VALUE_SEPARATOR, number, Const.LINE_SEPARATOR));
                    }
                    this.lock.writeUnlock();

                    return null;
                }
        );

        new Thread(futureTask).start();
        return futureTask;
    }

    public FutureTask<Boolean> delete(String name, String number) {
        FutureTask<Boolean> futureTask = new FutureTask<>(
                () -> {
                    File tempFile = new File("~tempfile.txt");
                    String recordToDeleteString = String.format("%s%s%s",name, Const.VALUE_SEPARATOR, number);
                    boolean result = false;

                    this.lock.writeLock();
                    try (
                            BufferedReader reader = new BufferedReader(new FileReader(filepath));
                            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
                    ) {
                        for (String recordString = reader.readLine(); recordString != null; recordString = reader.readLine()) {
                            String[] values = recordString.split(Const.VALUE_SEPARATOR);
                            String name_ = values[0], number_ = values[1];

                            if ((name == null || name.equals(name_)) && (number == null || number.equals(number_))) {
                                result = true;
                            } else {
                                writer.write(String.format("%s%s", recordString, Const.LINE_SEPARATOR));
                            }
                        }
                    }
                    this.lock.writeUnlock();
                    return result && new File(filepath).delete() && tempFile.renameTo(new File(filepath));
                }
        );

        new Thread(futureTask).start();
        return futureTask;
    }
}
