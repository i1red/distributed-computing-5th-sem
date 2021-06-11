package model.dao;

import model.entity.Player;
import model.mapper.PlayerMapper;
import model.table.PlayerTable;

public class PlayerDAO extends DAO<Player>{
    public PlayerDAO() {
        super(new PlayerMapper(), PlayerTable.NAME, PlayerTable.COLUMNS);
    }
}
