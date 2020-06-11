package spigot.greg.bwaddon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;
import org.screamingsandals.bedwars.utils.Title;

import java.util.*;

import static spigot.greg.bwaddon.I18n.*;

public class RunningTournament {
    public List<Round> roundList = new ArrayList<>();
    public Map<Phase, List<Round>> roundListByPhase = new HashMap<>();
    public List<Round> currentlyRunningRounds = new ArrayList<>();
    public List<Round> finishedRounds = new ArrayList<>();

    public RunningTournament(Tournament tournament) {
        List<Phase> phases = new ArrayList<>(tournament.getPhases());
        Phase[] orderedPhases = new Phase[phases.size()];
        int pos = 0;
        do {
            for (Phase phase : phases) {
                if ((pos == 0 && (phase.getParentPhase() == null || phase.getParentPhase().equals("")))
                        || (pos != 0 && orderedPhases[pos - 1].getCodeName().equals(phase.getParentPhase()))) {
                    orderedPhases[pos++] = phase;
                }
            }
            for (Phase phase : orderedPhases) {
                phases.remove(phase);
            }
        } while (!phases.isEmpty());

        // Generation round list
        for (Phase phase : orderedPhases) {
            List<String> possibleArenas = new ArrayList<>(phase.getPossibleArenas());
            Collections.shuffle(possibleArenas);

            if (phase.getParentPhase() == null || phase.getParentPhase().equals("")) {
                phase.calculatedRemainingTeams = tournament.getTeams().size() / phase.getTeams();
                int arenaPos = 0;
                for (int i = 0; i < (tournament.getTeams().size() / phase.getTeams()); i++) {
                    if (arenaPos >= possibleArenas.size()) {
                        arenaPos = 0;
                    }
                    Round round = new Round(phase, BedwarsAPI.getInstance().getGameByName(possibleArenas.get(arenaPos++)), false);
                    roundList.add(round);
                    if (!roundListByPhase.containsKey(phase)) {
                        roundListByPhase.put(phase, new ArrayList<>());
                    }
                    roundListByPhase.get(phase).add(round);
                }

                int index = 0;
                for (TournamentTeam team : tournament.getTeams()) {
                    if (index >= roundListByPhase.get(phase).size()) {
                        index = 0;
                    }
                    roundListByPhase.get(phase).get(index).addTeam(team);
                    index++;
                }
            } else {
                Phase parent = tournament.getPhase(phase.getParentPhase());
                int parentRemaining = parent.calculatedRemainingTeams;
                phase.calculatedRemainingTeams = parentRemaining / phase.getTeams();
                int arenaPos = 0;
                int parentRoundPos = 0;
                for (int i = 0; i < (parentRemaining / phase.getTeams()); i++) {
                    if (arenaPos >= possibleArenas.size()) {
                        arenaPos = 0;
                    }
                    Round round = new Round(phase, BedwarsAPI.getInstance().getGameByName(possibleArenas.get(arenaPos++)), phase == orderedPhases[orderedPhases.length - 1]);
                    roundList.add(round);
                    if (!roundListByPhase.containsKey(phase)) {
                        roundListByPhase.put(phase, new ArrayList<>());
                    }
                    roundListByPhase.get(phase).add(round);
                    for (int j = parentRoundPos; j < (parentRoundPos + phase.getTeams()); j++) {
                        Round round1 = roundListByPhase.get(parent).get(j);
                        round.addDependency(round1);
                        round1.setNextRound(round);
                    }
                    parentRoundPos += phase.getTeams();
                }
            }
        }
        roundList.forEach(System.out::println);

        Bukkit.getOnlinePlayers().forEach(player -> {
            Title.send(player, i18nonly("title_start").replace("%tournament%", BwAddon.getTournament().getName()), i18nonly("title_start_sub"));
            tournament.getTeams().forEach(tournamentTeam -> {
                if (tournamentTeam.getPlayers().contains(player.getUniqueId())) {
                    mpr("joined").replace("team", tournamentTeam.getTeamName()).send(player);
                    String players = "";
                    for (UUID uuid : tournamentTeam.getPlayers()) {
                        if (!players.equals("")) {
                            players += ", ";
                        }
                        players += Bukkit.getOfflinePlayer(uuid).getName();
                    }
                    mpr("members").replace("members", players).send(player);
                }
            });
        });

        runAllPossibleMatches();
    }

    public void runAllPossibleMatches() {
        System.out.println("Waiting rounds:");
        roundList.forEach(System.out::println);
        System.out.println("Running rounds:");
        currentlyRunningRounds.forEach(System.out::println);
        System.out.println("Finished rounds:");
        finishedRounds.forEach(System.out::println);

        for (Round round : new ArrayList<>(roundList)) {
            if (round.couldRun() && getGameRound(round.getRunningGame()) == null) {
                roundList.remove(round);
                currentlyRunningRounds.add(round);
                round.resolveDependencies();
                String teams = "";
                List<Player> players = new ArrayList<>();
                for (TournamentTeam team : round.getTeams()) {
                    if (!teams.equals("")) {
                        teams += ", ";
                    }
                    teams += team.getTeamName();
                    Bukkit.getScheduler().runTaskLater(BwAddon.getInstance(), () -> {
                        team.getPlayers().forEach(uuid -> {
                            Player player = Bukkit.getPlayer(uuid);
                            if (player != null) {
                                    Title.send(player, i18nonly("new_round_title"), i18nonly("new_round_subtitle"));
                            }
                        });
                    }, 20L);
                }
                Bukkit.broadcastMessage(i18n("new_round").replace("%teams%", teams));
            }
        }

        System.out.println("Waiting rounds:");
        roundList.forEach(System.out::println);
        System.out.println("Running rounds:");
        currentlyRunningRounds.forEach(System.out::println);
        System.out.println("Finished rounds:");
        finishedRounds.forEach(System.out::println);
    }

    public Round getGameRound(Game game) {
        for (Round round : currentlyRunningRounds) {
            if (game == round.getRunningGame()) {
                return round;
            }
        }
        return null;
    }

    public void findGameFor(Player player) {
        for (Round round : currentlyRunningRounds) {
            if (round.getRunningGame().getStatus() == GameStatus.WAITING) {
                for (TournamentTeam team : round.getTeams()) {
                    if (team.getPlayers().contains(player.getUniqueId())) {
                        round.getRunningGame().joinToGame(player);
                        mpr("game_found_tp").replace("game", round.getRunningGame().getName()).send(player);
                        Bukkit.getScheduler().runTaskLater(BwAddon.getInstance(), () -> {
                            Title.send(player, i18nonly("game_found"), i18nonly("game_found_tp").replace("%game%", round.getRunningGame().getName()));
                        }, 30L);
                        return;
                    }
                }
            }
        }
        mpr("no_match_for_you_yet").send(player);
    }
}
