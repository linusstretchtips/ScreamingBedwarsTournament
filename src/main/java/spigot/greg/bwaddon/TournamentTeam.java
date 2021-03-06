package spigot.greg.bwaddon;

import org.screamingsandals.bedwars.api.game.Game;

import java.util.*;

public class TournamentTeam {
    private String teamName;
    private List<UUID> players = new ArrayList<>();

    public TournamentTeam(String teamName) {
        this.teamName = teamName;
    }

    public String currentInGameTeam;
    public Game currentGame;

    public void addPlayer(UUID player) {
        players.add(player);
    }

    public void removePlayer(UUID player) {
        players.remove(player);
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public String getTeamName() {
        return teamName;
    }
}
