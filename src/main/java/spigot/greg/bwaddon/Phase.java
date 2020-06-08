package spigot.greg.bwaddon;

import java.util.ArrayList;
import java.util.List;

public class Phase {
    private String codeName;
    private String parentPhase = null;
    private int teams = 4;
    private List<String> possibleArenas = new ArrayList<>();
    public int calculatedRemainingTeams;

    public Phase(String codeName) {
        this.codeName = codeName;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getParentPhase() {
        return parentPhase;
    }

    public void setParentPhase(String parentPhase) {
        this.parentPhase = parentPhase;
    }

    public void setTeams(int teams) {
        this.teams = teams;
    }

    public int getTeams() {
        return teams;
    }

    public List<String> getPossibleArenas() {
        return possibleArenas;
    }

    public void addPossibleArena(String arena) {
        if (!possibleArenas.contains(arena)) {
            possibleArenas.add(arena);
        }
    }

    public void removePossibleArena(String arena) {
        if (possibleArenas.contains(arena)) {
            possibleArenas.add(arena);
        }
    }
}
