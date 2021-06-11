package model.table;

import java.util.Arrays;
import java.util.List;

public class PlayerTable {
    public static final String NAME = "player";

    public static class Column {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String TEAM_ID = "team_id";
    }

    public static final List<String> COLUMNS = Arrays.asList(Column.ID, Column.NAME, Column.TEAM_ID);
}
