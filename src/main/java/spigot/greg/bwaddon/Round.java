package spigot.greg.bwaddon;

import org.screamingsandals.bedwars.api.game.Game;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private Phase phase;
    private List<Round> dependsOn = new ArrayList<>();
    private List<TournamentTeam> teams = new ArrayList<>();
    private boolean running = false;
    private boolean finalRound;
    private Game runningGame;
    private Round nextRound;
    private TournamentTeam winner;
    private boolean force = false;

    public Round(Phase phase, Game runningGame, boolean finalRound) {
        this.phase = phase;
        this.runningGame = runningGame;
        this.finalRound = finalRound;
    }

    public List<TournamentTeam> getTeams() {
        return teams;
    }

    public void addDependency(Round round) {
        if (!dependsOn.contains(round)) {
            dependsOn.add(round);
        }
    }

    public void addTeam(TournamentTeam team) {
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isFinalRound() {
        return finalRound;
    }

    public Game getRunningGame() {
        return runningGame;
    }

    public Phase getPhase() {
        return phase;
    }

    public Round getNextRound() {
        return nextRound;
    }

    public void setNextRound(Round nextRound) {
        this.nextRound = nextRound;
    }

    public TournamentTeam getWinner() {
        return winner;
    }

    public void setWinner(TournamentTeam winner) {
        this.winner = winner;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public boolean isForce() {
        return force;
    }

    public int getCalculatedPlayers() {
        int calc = 0;
        for (TournamentTeam team : teams) {
            calc += team.getPlayers().size();
        }
        return calc;
    }

    public boolean couldRun() {
        for (Round parent : dependsOn) {
            if (parent.getWinner() == null) {
                return false;
            }
        }
        return true;
    }

    public void resolveDependencies() {
        for (Round parent : dependsOn) {
            addTeam(parent.getWinner());
        }
    }

    @Override
    public String toString() {
        return "Round{" +
                "phase=" + phase +
                ", dependsOn=" + dependsOn +
                ", teams=" + teams +
                ", running=" + running +
                ", finalRound=" + finalRound +
                ", runningGame=" + runningGame +
                ", winner=" + winner +
                '}';
    }
}
