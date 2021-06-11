package model.mapper;

import model.entity.Player;
import model.table.PlayerTable;
import model.table.TeamTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PlayerMapper implements Mapper<Player>{
    @Override
    public Player fromResultSet(ResultSet resultSet) throws SQLException {
        return new Player()
                .setId(resultSet.getInt(PlayerTable.Column.ID))
                .setName(resultSet.getString(PlayerTable.Column.NAME))
                .setTeamId(resultSet.getInt(PlayerTable.Column.TEAM_ID));
    }

    @Override
    public void fillPreparedStatement(Player player, PreparedStatement preparedStatement, List<String> columnNames) throws SQLException {
        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            switch (columnNames.get(columnIndex)) {
                case PlayerTable.Column.ID -> preparedStatement.setInt(columnIndex + 1, player.getId());
                case PlayerTable.Column.NAME -> preparedStatement.setString(columnIndex + 1, player.getName());
                case PlayerTable.Column.TEAM_ID -> preparedStatement.setInt(columnIndex + 1, player.getTeamId());
                default -> throw new IllegalArgumentException(String.format("Column %s does not exist", columnNames.get(columnIndex)));
            }
        }
    }
}
