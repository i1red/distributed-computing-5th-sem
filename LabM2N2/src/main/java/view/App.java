package view;

import model.dao.PlayerDAO;
import model.dao.TeamDAO;
import model.entity.Player;
import model.entity.Team;
import model.table.PlayerTable;
import model.table.TeamTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class App extends JFrame{
    private static final String TEAMS_TAB_TITLE = "Teams";
    private static final String PLAYERS_TAB_TITLE = "Players";

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    private JPanel teamsPanel;
    private JList<String> teamsList;
    private JTextField teamTextField;
    private JButton createTeamBtn;

    private JPanel playersPanel;
    private JComboBox<String> teamsComboBox;
    private JList<String> playersList;
    private JTextField playerTextField;
    private JButton createPlayerBtn;

    // non GUI
    private final DefaultListModel<String> teamsModel = new DefaultListModel<>();
    private final DefaultListModel<String> playersModel = new DefaultListModel<>();

    public App() {
        setUpModels();
        addListeners();

        try {
            fillTeamsList();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(mainPanel, "Failed to load teams");
        }
    }

    private void setUpModels() {
        teamsList.setModel(teamsModel);
        playersList.setModel(playersModel);
    }

    private void addListeners() {
        createTeamBtn.addActionListener(
                e -> {
                    try {
                        createTeam();
                        fillTeamsList();
                    } catch (SQLException exception) {
                        JOptionPane.showMessageDialog(mainPanel, "Failed to create team");
                    }
                }
        );

        createPlayerBtn.addActionListener(
                e -> {
                    try {
                        createPlayer();
                        fillPlayersList();
                    } catch (SQLException exception) {
                        JOptionPane.showMessageDialog(mainPanel, "Failed to create player");
                    }
                }
        );

        tabbedPane.addChangeListener(
                l -> {
                    String tabTitle = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
                    if (tabTitle.equals(PLAYERS_TAB_TITLE)) {

                        try {
                            fillTeamComboBox();
                            fillPlayersList();
                        } catch (SQLException exception) {
                            JOptionPane.showMessageDialog(mainPanel, "Failed to load info");
                        }
                    }
                }
        );

        teamsComboBox.addActionListener(
                e -> {
                    try {
                        fillPlayersList();
                    } catch (SQLException exception) {
                        JOptionPane.showMessageDialog(mainPanel, "Failed to load info");
                    }
                }
        );

    }

    private void createTeam() throws SQLException {
        String teamName = teamTextField.getText();
        new TeamDAO().insert(new Team().setName(teamName));
    }


    private void createPlayer() throws SQLException {
        String teamName = getTeamNameFromComboBox();
        if (teamName == null) {
            return;
        }

        String playerName = playerTextField.getText();

        new PlayerDAO().insert(new Player().setName(playerName).setTeamId(getTeamByName(teamName).getId()));
    }


    private void fillTeamComboBox() throws SQLException {
        teamsComboBox.removeAllItems();

        for (Team team: new TeamDAO().list()) {
            teamsComboBox.addItem(team.getName());
        }
    }

    private void fillTeamsList() throws SQLException {
        teamsModel.clear();
        teamsModel.addAll(new TeamDAO().list().stream().map(Team::getName).collect(Collectors.toList()));
    }

    private void fillPlayersList() throws SQLException {
        String teamName = getTeamNameFromComboBox();
        if (getTeamNameFromComboBox() == null) {
            return;
        }

        playersModel.clear();

        List<Player> players = new PlayerDAO().filter(
                Collections.singletonList(PlayerTable.Column.TEAM_ID),
                new Player().setTeamId(getTeamByName(teamName).getId())
        );

        playersModel.addAll(players.stream().map(Player::getName).collect(Collectors.toList()));
    }

    private String getTeamNameFromComboBox() {
        Object teamItem = teamsComboBox.getSelectedItem();
        return teamItem == null ? null : (String) teamItem;
    }

    private Team getTeamByName(String teamName) throws SQLException {
        return new TeamDAO().filter(
                Collections.singletonList(TeamTable.Column.NAME),
                new Team().setName(teamName)
        ).get(0);
    }


    public static void main(String[] args) {
        var app = new App();

        var frame = new JFrame ("Football");
        frame.setContentPane(app.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
