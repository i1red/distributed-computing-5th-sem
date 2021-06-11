package model.table;

import java.util.Arrays;
import java.util.List;

public class TeamTable {
    public static final String NAME = "team";

    public static class Column {
        public static final String ID = "id";
        public static final String NAME = "name";
    }

    public static final List<String> COLUMNS = Arrays.asList(Column.ID, Column.NAME);
}
