package spigot.greg.bwaddon;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.events.*;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.ArrayList;

public class BedwarsListener implements Listener {
    @EventHandler
    public void onBWEnable(PluginEnableEvent event) {
        if (event.getPlugin().getName().equals("BedWars")) {
            new AdminCommands(); // re register commands
        }
    }

    @EventHandler
    public void onPlayerJoinBedwars(BedwarsPlayerJoinEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (BwAddon.getTournament().isActive()) {
            Game game = event.getGame();
            RunningTournament tournament = BwAddon.getTournament().getRunningTournament();
            tournament.currentlyRunningRounds.forEach(round -> {
                if (round.getRunningGame() == event.getGame()) {
                    event.setCancelled(true);
                    round.getTeams().forEach(tournamentTeam -> {
                        if (tournamentTeam.getPlayers().contains(event.getPlayer().getUniqueId())) {
                            event.setCancelled(false);
                            event.getPlayer().sendMessage("§6[BW Tournament] §aWelcome in tournament match! You will be connected to team: " + tournamentTeam.getTeamName());
                            if (tournamentTeam.currentInGameTeam == null) {
                                for (Team team : game.getAvailableTeams()) {
                                    boolean isFree = true;
                                    for (TournamentTeam t : round.getTeams()) {
                                        if (t.currentInGameTeam.equals(team.getName())) {
                                            isFree = false;
                                            break;
                                        }
                                    }
                                    if (isFree) {
                                        tournamentTeam.currentInGameTeam = team.getName();
                                        break;
                                    }
                                }
                            }
                            game.selectPlayerTeam(event.getPlayer(), game.getTeamFromName(tournamentTeam.currentInGameTeam));
                        }
                    });
                }
            });
        }
    }

    @EventHandler
    public void onPlayerJoinedTeam(BedwarsPlayerJoinTeamEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (BwAddon.getTournament().isActive()) {
            Game game = event.getGame();
            RunningTournament tournament = BwAddon.getTournament().getRunningTournament();
            tournament.currentlyRunningRounds.forEach(round -> {
                if (round.getRunningGame() == event.getGame()) {
                    round.getTeams().forEach(tournamentTeam -> {
                        if (!tournamentTeam.currentInGameTeam.equals(event.getTeam().getName())) {
                            event.setCancelled(true);
                        }
                    });
                }
            });
        }
    }

    @EventHandler
    public void onGameEnds(BedwarsGameEndingEvent event) {
        if (BwAddon.getTournament().isActive()) {
            RunningTournament tournament = BwAddon.getTournament().getRunningTournament();
            new ArrayList<>(tournament.currentlyRunningRounds).forEach(round -> {
                if (round.getRunningGame() == event.getGame()) {
                    for (TournamentTeam t : round.getTeams()) {
                        if (t.currentInGameTeam.equals(event.getWinningTeam().getName())) {
                            round.setWinner(t);
                            t.getPlayers().forEach(uuid -> {
                                Bukkit.getPlayer(uuid).sendMessage("§6[BW Tournament] §aCongratulations! Your team won the match and can continue!");
                            });
                        }
                        t.currentGame = null;
                        t.currentInGameTeam = null;
                    }
                    round.setRunning(false);
                    tournament.currentlyRunningRounds.remove(round);
                    tournament.finishedRounds.add(round);
                }
            });
        }
    }

    @EventHandler
    public void onGameTick(BedwarsGameTickEvent event) {

    }

    @EventHandler
    public void afterRebuild(BedwarsPostRebuildingEvent event) {

    }
}