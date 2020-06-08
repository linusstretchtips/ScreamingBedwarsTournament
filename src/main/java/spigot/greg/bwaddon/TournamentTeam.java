package spigot.greg.bwaddon;

import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.*;

public class TournamentTeam {
    private String teamName;
    private List<UUID> players = new ArrayList<>();
    private boolean alive = true;

    public RunningTeam currentInGameTeam;
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

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public String getTeamName() {
        return teamName;
    }
}
