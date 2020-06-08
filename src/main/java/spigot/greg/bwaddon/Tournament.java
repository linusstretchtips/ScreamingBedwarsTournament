package spigot.greg.bwaddon;

import java.util.ArrayList;
import java.util.List;

public class Tournament {
    private String name = "Tournament";
    private List<TournamentTeam> teams = new ArrayList<>();
    private boolean active = false;
    private List<Phase> phases = new ArrayList<>();

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
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addPhase(Phase phase) {
        if (!phases.contains(phase)) {
            phases.add(phase);
        }
    }

    public void removePhase(Phase phase) {
        if (phases.contains(phase)) {
            phases.remove(phase);
        }
    }

    public void setName(String name) {
        this.name = name;
    }
}
