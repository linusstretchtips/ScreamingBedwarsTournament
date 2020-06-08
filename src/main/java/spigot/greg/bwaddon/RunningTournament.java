package spigot.greg.bwaddon;

import org.bukkit.Bukkit;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.utils.Title;

import java.util.*;

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
                    for (int j = arenaPos; j < (arenaPos + phase.getTeams()); j++) {
                        Round round1 = roundListByPhase.get(parent).get(j);
                        round.addDependency(round1);
                        round1.setNextRound(round);
                    }
                    arenaPos += phase.getTeams();
                }
            }
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            Title.send(player, "§aWelcome on BedWars tournament", "§cIf you are part of some team, join to the game now via the NPC on the spawn");
            tournament.getTeams().forEach(tournamentTeam -> {
                if (tournamentTeam.getPlayers().contains(player.getUniqueId())) {
                    player.sendMessage("§6[BW Tournament] §aYou joined the tournament with team §7" + tournamentTeam.getTeamName());
                    String players = "";
                    for (UUID uuid : tournamentTeam.getPlayers()) {
                        if (!players.equals("")) {
                            players += ", ";
                        }
                        players += Bukkit.getPlayer(uuid).getName();
                    }
                    player.sendMessage("§6[BW Tournament] §aTeam members: §7" + players);
                }
            });
        });
    }
}