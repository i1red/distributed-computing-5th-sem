package model.mapper;

import model.entity.Team;
import model.table.TeamTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TeamMapper implements Mapper<Team>{
    @Override
    public Team fromResultSet(ResultSet resultSet) throws SQLException {
        return new Team()
                .setId(resultSet.getInt(TeamTable.Column.ID))
                .setName(resultSet.getString(TeamTable.Column.NAME));
    }

    @Override
    public void fillPreparedStatement(Team team, PreparedStatement preparedStatement, List<String> columnNames) throws SQLException {
        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            switch (columnNames.get(columnIndex)) {
                case TeamTable.Column.ID -> preparedStatement.setInt(columnIndex + 1, team.getId());
                case TeamTable.Column.NAME -> preparedStatement.setString(columnIndex + 1, team.getName());
                default -> throw new IllegalArgumentException(String.format("Column %s does not exist", columnNames.get(columnIndex)));
            }
        }
    }
}
