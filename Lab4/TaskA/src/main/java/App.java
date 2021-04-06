import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class App {
    private static final String DATABASE_FILENAME = "database.txt";

    private static String getDatabaseFilepath() throws IOException {
        Enumeration<URL> resources = App.class.getClassLoader().getResources(DATABASE_FILENAME);
        return resources.nextElement().getFile();
    }

    private static void printDatabaseContent() throws IOException {
        System.out.println();
        try (BufferedReader reader = new BufferedReader(new FileReader(getDatabaseFilepath()))) {
            for (String recordString = reader.readLine(); recordString != null; recordString = reader.readLine()) {
                System.out.println(recordString);
            }
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        String databaseFilepath = getDatabaseFilepath();

        CustomReadWriteLock lock = new CustomReadWriteLock();
        Reader reader = new Reader(databaseFilepath, lock);
        Writer writer = new Writer(databaseFilepath, lock);

        FutureTask<Void> addIvanTask = writer.add("Ivan", "12345");
        FutureTask<Void> addTarasTask = writer.add("Taras", "54321");
        FutureTask<Void> addBorysTask = writer.add("Borys", "23451");

        addIvanTask.get();
        addTarasTask.get();
        addBorysTask.get();

        printDatabaseContent();

        FutureTask<String> findBorysNumberTask = reader.findNumberByName("Borys");
        FutureTask<String> findIvanNumberTask = reader.findNumberByName("Ivan");
        FutureTask<String> find54321NameTask = reader.findNameByNumber("54321");

        System.out.printf("Borys number: %s\n", findBorysNumberTask.get());
        System.out.printf("Ivan number: %s\n", findIvanNumberTask.get());
        System.out.printf("Person with number 54321: %s\n", find54321NameTask.get());

        FutureTask<Boolean> deleteBorys = writer.delete("Borys", null);
        FutureTask<Boolean> delete00000 = writer.delete(null, "00000");

        System.out.printf("Borys deletion result: %s\n", deleteBorys.get());
        System.out.printf("00000 deletion result: %s\n", delete00000.get());

        printDatabaseContent();

        System.out.printf("Borys number (2nd try): %s\n", reader.findNumberByName("Borys").get());
    }
}
