package spigot.greg.bwaddon;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.events.*;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;

import java.util.ArrayList;
import java.util.UUID;

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
        if (BwAddon.getTournament().isActive() && event.getGame().getStatus() == GameStatus.WAITING) {
            Game game = event.getGame();
            RunningTournament tournament = BwAddon.getTournament().getRunningTournament();
            tournament.currentlyRunningRounds.forEach(round -> {
                if (round.getRunningGame() == event.getGame()) {
                    event.setCancelMessage("§6You are not part of this round! You can return while game is running as spectator!");
                    event.setCancelled(true);
                    round.getTeams().forEach(tournamentTeam -> {
                        if (tournamentTeam.getPlayers().contains(event.getPlayer().getUniqueId())) {
                            event.setCancelled(false);
                            event.getPlayer().sendMessage("§6[BW Tournament] §aWelcome in tournament match! You will be connected to team: " + tournamentTeam.getTeamName());
                            if (tournamentTeam.currentInGameTeam == null) {
                                for (Team team : game.getAvailableTeams()) {
                                    boolean isFree = true;
                                    for (TournamentTeam t : round.getTeams()) {
                                        if (t.currentInGameTeam != null && t.currentInGameTeam.equals(team.getName())) {
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
            RunningTournament tournament = BwAddon.getTournament().getRunningTournament();
            tournament.currentlyRunningRounds.forEach(round -> {
                if (round.getRunningGame() == event.getGame()) {
                    round.getTeams().forEach(tournamentTeam -> {
                        if (tournamentTeam.getPlayers().contains(event.getPlayer().getUniqueId())) {
                            if (tournamentTeam.currentInGameTeam != null && !tournamentTeam.currentInGameTeam.equals(event.getTeam().getName())) {
                                event.setCancelled(true);
                            }
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
                            if (round.isFinalRound()) {
                                Bukkit.broadcastMessage("§6[BW Tournament] §aTeam §7" + t.getTeamName() + "§a won this tournament!");
                                String players = "";
                                for (UUID uuid : t.getPlayers()) {
                                    if (!players.equals("")) {
                                        players += ", ";
                                    }
                                    players += Bukkit.getPlayer(uuid).getName();
                                }
                                Bukkit.broadcastMessage("§6[BW Tournament] §aHeroes of this tournament: §7" + players);
                                Bukkit.broadcastMessage("§6[BW Tournament] §cThank you for visiting this tournament! If you are insterested in this tournaments,");
                                Bukkit.broadcastMessage("§6[BW Tournament] §cyou can support us on our patreon: §7https://www.patreon.com/screamingsandals");
                                Bukkit.broadcastMessage("§3Scream!");
                            }
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
        if (BwAddon.getTournament().isActive()) {
            if (event.getStatus() == GameStatus.WAITING) {
                RunningTournament tournament = BwAddon.getTournament().getRunningTournament();
                tournament.currentlyRunningRounds.forEach(round -> {
                    if (round.getRunningGame() == event.getGame()) {
                        if (event.getGame().countConnectedPlayers() < round.calculatedPlayers) {
                            event.setNextCountdown(event.getGame().getLobbyCountdown());
                            event.setNextStatus(GameStatus.WAITING);
                        }
                    }
                });
            }
        }
    }

    @EventHandler
    public void afterRebuild(BedwarsPostRebuildingEvent event) {
        Bukkit.getScheduler().runTaskLater(BwAddon.getInstance(), () -> {
            if (BwAddon.getTournament().isActive()) {
                BwAddon.getTournament().getRunningTournament().runAllPossibleMatches();
            }
        }, 20L);
    }
}
