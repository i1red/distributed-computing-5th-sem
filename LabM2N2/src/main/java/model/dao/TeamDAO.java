package model.dao;

import model.entity.Team;
import model.mapper.TeamMapper;
import model.table.TeamTable;

public class TeamDAO extends DAO<Team> {
    public TeamDAO() {
        super(new TeamMapper(), TeamTable.NAME, TeamTable.COLUMNS);
    }
}
