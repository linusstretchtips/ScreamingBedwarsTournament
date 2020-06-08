package spigot.greg.bwaddon;

import org.bukkit.Bukkit;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.ArrayList;
import java.util.List;

public class Tournament {
    private String name = "Tournament";
    private List<TournamentTeam> teams = new ArrayList<>();
    private List<Phase> phases = new ArrayList<>();
    private RunningTournament runningTournament = null;

    public String getName() {
        return name;
    }

    public List<TournamentTeam> getTeams() {
        return teams;
    }

    public void addTeam(TournamentTeam team) {
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }

    public void removeTeam(TournamentTeam team) {
        if (teams.contains(team)) {
            teams.remove(team);
        }
    }

    public boolean hasTeam(String name) {
        return getTeam(name) != null;
    }

    public TournamentTeam getTeam(String name) {
        for (TournamentTeam team : teams) {
            if (team.getTeamName().equals(name)) {
                return team;
            }
        }
        return null;
    }

    public boolean isActive() {
        return runningTournament != null;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public void addPhase(Phase phase) {
        if (!phases.contains(phase)) {
            phases.add(phase);
        }
    }

    public boolean hasPhase(String name) {
        return getPhase(name) != null;
    }

    public Phase getPhase(String name) {
        for (Phase phase : phases) {
            if (phase.getCodeName().equals(name)) {
                return phase;
            }
        }
        return null;
    }

    public void removePhase(Phase phase) {
        if (phases.contains(phase)) {
            phases.remove(phase);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void startTournament() {
        runningTournament = new RunningTournament(this);
    }

    public void stopTournament() {
        runningTournament = null;
        Bukkit.getOnlinePlayers().forEach(player -> {
            Game game = BedwarsAPI.getInstance().getGameOfPlayer(player);
            if (game != null) {
                game.leaveFromGame(player);
            }
        });
    }

    public RunningTournament getRunningTournament() {
        return runningTournament;
    }
}
