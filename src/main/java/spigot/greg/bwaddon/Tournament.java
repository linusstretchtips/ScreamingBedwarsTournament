package spigot.greg.bwaddon;

import java.util.ArrayList;
import java.util.List;

public class Tournament {
    private String name = "Tournament";
    private List<TournamentTeam> teams = new ArrayList<>();
    private boolean active = false;
    private List<Round> rounds = new ArrayList<>();

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addRound(Round round) {
        if (!rounds.contains(round)) {
            rounds.add(round);
        }
    }

    public void removeRound(Round round) {
        if (rounds.contains(round)) {
            rounds.remove(round);
        }
    }
}
