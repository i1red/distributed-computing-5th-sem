import java.util.ArrayList;
import java.util.HashMap;

public class EqualQuantityCheck implements Runnable {
    @Override
    public void run() {
        ArrayList<HashMap<Character, Integer>> lettersCounts = countLettersInStrings(App.getStrings());

        System.out.println();

        if (checkIfDone(lettersCounts, App.getStrings())) {
            for (Thread thread: App.getThreads()) {
                thread.interrupt();
            }
        }
    }

    private ArrayList<HashMap<Character, Integer>> countLettersInStrings(ArrayList<StringBuilder> stringBuilders) {
        var lettersCounts = new ArrayList<HashMap<Character, Integer>>();

        for (StringBuilder stringBuilder: stringBuilders) {
            lettersCounts.add(countLettersInString(stringBuilder));
        }

        return lettersCounts;
    }

    private HashMap<Character, Integer> countLettersInString(StringBuilder stringBuilder) {
        var lettersCount = new HashMap<Character, Integer>(){{put('A', 0); put('B', 0);}};

        for (int i = 0; i < stringBuilder.length(); i++) {
            Character currChar = stringBuilder.charAt(i);
            if (currChar.equals('A') || currChar.equals('B')) {
                lettersCount.put(currChar, lettersCount.get(currChar) + 1);
            }
        }

        return lettersCount;
    }

    private boolean checkIfDone(ArrayList<HashMap<Character, Integer>> lettersCounts, ArrayList<StringBuilder> stringBuilders) {
        int aTotalCount = 0, bTotalCount = 0;

        for (HashMap<Character, Integer> lettersCount : lettersCounts) {
            aTotalCount += lettersCount.get('A');
            bTotalCount += lettersCount.get('B');
        }

        for (int i = 0; i < lettersCounts.size(); i++) {
            HashMap<Character, Integer> lettersCount = lettersCounts.get(i);
            if (aTotalCount - lettersCount.get('A') == bTotalCount - lettersCount.get('B')) {
                System.out.printf("Finished successfully, resulted strings: %s\n",
                        getResultedStrings(lettersCounts, stringBuilders, i));
                return true;
            }
        }
        return false;
    }

    private String getResultedStrings(ArrayList<HashMap<Character, Integer>> lettersCounts,
                                      ArrayList<StringBuilder> stringBuilders, int ignoredStringIndex) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < lettersCounts.size(); i++) {
            if (i != ignoredStringIndex) {
                HashMap<Character, Integer> lettersCount = lettersCounts.get(i);
                stringBuilder.append(String.format(
                        "%s: A - %d, B - %d",
                        stringBuilders.get(i).toString(), lettersCount.get('A'), lettersCount.get('B')
                )).append("; ");
            }
        }

        return stringBuilder.toString();
    }
}
