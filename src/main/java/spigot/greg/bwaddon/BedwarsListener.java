package spigot.greg.bwaddon;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.events.*;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;
import org.screamingsandals.bedwars.utils.Title;

import java.util.ArrayList;
import java.util.UUID;

import static spigot.greg.bwaddon.I18n.*;

public class BedwarsListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (BwAddon.getTournament().isActive()) {
            Title.send(event.getPlayer(), i18nonly("title_start").replace("%tournament%", BwAddon.getTournament().getName()), i18nonly("title_start_sub"));
            BwAddon.getTournament().getTeams().forEach(tournamentTeam -> {
                if (tournamentTeam.getPlayers().contains(event.getPlayer().getUniqueId())) {
                    mpr("joined").replace("team", tournamentTeam.getTeamName()).send(event.getPlayer());
                    String players = "";
                    for (UUID uuid : tournamentTeam.getPlayers()) {
                        if (!players.equals("")) {
                            players += ", ";
                        }
                        players += Bukkit.getOfflinePlayer(uuid).getName();
                    }
                    mpr("members").replace("members", players).send(event.getPlayer());
                }
            });
        }
    }

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
                    event.setCancelMessage(i18nonly("why_are_you_here"));
                    event.setCancelled(true);
                    round.getTeams().forEach(tournamentTeam -> {
                        if (tournamentTeam.getPlayers().contains(event.getPlayer().getUniqueId())) {
                            event.setCancelled(false);
                            mpr("welcome_in_team").replace("team", tournamentTeam.getTeamName()).send(event.getPlayer());
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
                            if (tournamentTeam.currentInGameTeam != null && event.getTeam() != null && !tournamentTeam.currentInGameTeam.equals(event.getTeam().getName())) {
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
                        if (t.currentInGameTeam != null && t.currentInGameTeam.equals(event.getWinningTeam().getName())) {
                            round.setWinner(t);
                            t.getPlayers().forEach(uuid -> {
                                mpr("won").send(Bukkit.getPlayer(uuid));
                            });
                            if (round.isFinalRound()) {
                                Bukkit.broadcastMessage(i18n("tournament_won").replace("%team%", t.getTeamName()));
                                String players = "";
                                for (UUID uuid : t.getPlayers()) {
                                    if (!players.equals("")) {
                                        players += ", ";
                                    }
                                    players += Bukkit.getOfflinePlayer(uuid).getName();
                                }
                                Bukkit.broadcastMessage(i18n("tournament_heroes").replace("%winners%", players));
                                Bukkit.broadcastMessage(i18nonly("self_promote_one"));
                                Bukkit.broadcastMessage(i18nonly("self_promote_two"));
                                Bukkit.broadcastMessage(i18nonly("self_promote_three"));
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
                        if (event.getGame().countConnectedPlayers() < round.getCalculatedPlayers()) {
                            event.setNextCountdown(event.getGame().getLobbyCountdown());
                            event.setNextStatus(GameStatus.WAITING);
                        } else if (event.getCountdown() > 10) {
                            event.setNextCountdown(10);
                        }
                    }
                });
            }
        }
    }

    @EventHandler
    public void onOpeningTeamSelection(BedwarsOpenTeamSelectionEvent event) {
        if (BwAddon.getTournament().isActive()) {
            RunningTournament tournament = BwAddon.getTournament().getRunningTournament();
            tournament.currentlyRunningRounds.forEach(round -> {
                if (round.getRunningGame() == event.getGame()) {
                    event.setCancelled(true);
                    mpr("you_cant_select_team").send(event.getPlayer());
                }
            });
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
