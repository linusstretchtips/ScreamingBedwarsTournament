package spigot.greg.bwaddon;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.commands.BaseCommand;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdminCommands extends BaseCommand {

    protected AdminCommands() {
        super("tournament", "bw.tournament.admin", false);
    }

    @Override
    public boolean execute(CommandSender commandSender, List<String> list) {
        if (list.size() == 0 || (list.size() == 1 && list.get(0).equalsIgnoreCase("help"))) {
            commandSender.sendMessage("Tournament help:");
            commandSender.sendMessage("/bw tournament name <name> - Sets tournament name");
            commandSender.sendMessage("/bw tournament addteam <name> - Adds new team to the tournament");
            commandSender.sendMessage("/bw tournament join <team> <player> - Adds player to the team in tournament");
            commandSender.sendMessage("/bw tournament leave <team> <player> - Removes player from the team in tournament");
            commandSender.sendMessage("/bw tournament removeteam <name> - Removes team in tournament");
            commandSender.sendMessage("/bw tournament phase add <code name> - Adds phase");
            commandSender.sendMessage("/bw tournament phase addarena <code name> <arena> - Adds arena to phase");
            commandSender.sendMessage("/bw tournament phase removearena <code name> <arena> - Removes arena to phase");
            commandSender.sendMessage("/bw tournament phase parent <code name> <parent code name> - Sets parent phase (if no phase is parent, this is first phase)");
            commandSender.sendMessage("/bw tournament phase noparent <code name> - Removes parent phase (only one phase must be without parent");
            commandSender.sendMessage("/bw tournament phase teams <code name> <teams> - Sets number of teams per game");
            commandSender.sendMessage("/bw tournament phase remove <code name> - Removes phase");
            commandSender.sendMessage("/bw tournament start - Starts the tournament");
            commandSender.sendMessage("/bw tournament stop - Stops the tournament");
            commandSender.sendMessage("/bw tournament clear - Clears tournament settings");
            commandSender.sendMessage("/bw tournament forceRoundLoad - If something went wrong and tournament just stops, use this command!");
            commandSender.sendMessage("/bw tournament save and /bw tournament load");
            return true;
        } else if (list.size() >= 1) {
            if (BwAddon.getTournament().isActive()) {
                if (list.get(0).equalsIgnoreCase("stop")) {
                    BwAddon.getTournament().stopTournament();
                    return true;
                } else if (list.get(0).equalsIgnoreCase("forceRoundLoad")) {
                    BwAddon.getTournament().getRunningTournament().runAllPossibleMatches();
                    return true;
                }
            } else {
                if (list.get(0).equalsIgnoreCase("name") && list.size() > 1) {
                    BwAddon.getTournament().setName(list.get(1));
                    commandSender.sendMessage("§aYay, you made it! It's now §7" + list.get(1));
                    return true;
                } else if (list.get(0).equalsIgnoreCase("addteam") && list.size() > 1) {
                    if (!BwAddon.getTournament().hasTeam(list.get(1))) {
                        BwAddon.getTournament().addTeam(new TournamentTeam(list.get(1)));
                        commandSender.sendMessage("§aNew motherfuckers are now part of the tournament:§7 " + list.get(1));
                    } else {
                        commandSender.sendMessage("§cThese motherfuckers are already part of the tournament:§7 " + list.get(1));
                    }
                    return true;
                } else if (list.get(0).equalsIgnoreCase("join") && list.size() > 2) {
                    if (BwAddon.getTournament().hasTeam(list.get(1))) {
                        OfflinePlayer player = Bukkit.getOfflinePlayer(list.get(2));
                        BwAddon.getTournament().getTeam(list.get(1)).addPlayer(player.getUniqueId());
                        commandSender.sendMessage("§aMotherfucker §7" + player.getName() + " (" + player.getUniqueId() + ") §ais now part of team §7" + list.get(1));
                    } else {
                        commandSender.sendMessage("§cThese motherfuckers have never been in this tournament:§7 " + list.get(1));
                    }
                    return true;
                } else if (list.get(0).equalsIgnoreCase("leave")) {
                    if (BwAddon.getTournament().hasTeam(list.get(1))) {
                        OfflinePlayer player = Bukkit.getOfflinePlayer(list.get(2));
                        BwAddon.getTournament().getTeam(list.get(1)).removePlayer(player.getUniqueId());
                        commandSender.sendMessage("§aMotherfucker §7" + player.getName() + " (" + player.getUniqueId() + ") §ais no longer part of team §7" + list.get(1));
                    } else {
                        commandSender.sendMessage("§cThese motherfuckers have never been in this tournament:§7 " + list.get(1));
                    }
                    return true;
                } else if (list.get(0).equalsIgnoreCase("removeteam")) {
                    if (BwAddon.getTournament().hasTeam(list.get(1))) {
                        BwAddon.getTournament().removeTeam(BwAddon.getTournament().getTeam(list.get(1)));
                        commandSender.sendMessage("§aThese motherfuckers are no longer playing this tournament:§7 " + list.get(1));
                    } else {
                        commandSender.sendMessage("§cThese motherfuckers have never been in this tournament:§7 " + list.get(1));
                    }
                    return true;
                } else if (list.get(0).equalsIgnoreCase("phase") && list.size() > 2) {
                    if (list.get(1).equalsIgnoreCase("add")) {
                        if (!BwAddon.getTournament().hasPhase(list.get(2))) {
                            BwAddon.getTournament().addPhase(new Phase(list.get(2)));
                            commandSender.sendMessage("§aNew phase is here:§7 " + list.get(2));
                        } else {
                            commandSender.sendMessage("§cThis phase already exists:§7 " + list.get(2));
                        }
                        return true;
                    } else if (list.get(1).equalsIgnoreCase("addarena") && list.size() > 3) {
                        if (BwAddon.getTournament().hasPhase(list.get(2))) {
                            BwAddon.getTournament().getPhase(list.get(2)).addPossibleArena(list.get(3));
                            commandSender.sendMessage("§aArena §7" + list.get(3) + "§a was added to the phase: §7" + list.get(2));
                        } else {
                            commandSender.sendMessage("§cThis phase has never been in this tournament:§7 " + list.get(2));
                        }
                        return true;
                    } else if (list.get(1).equalsIgnoreCase("removearena") && list.size() > 3) {
                        if (BwAddon.getTournament().hasPhase(list.get(2))) {
                            BwAddon.getTournament().getPhase(list.get(2)).removePossibleArena(list.get(3));
                            commandSender.sendMessage("§aArena §7" + list.get(3) + "§a was removed from the phase: §7" + list.get(2));
                        } else {
                            commandSender.sendMessage("§cThis phase has never been in this tournament:§7 " + list.get(2));
                        }
                        return true;
                    } else if (list.get(1).equalsIgnoreCase("parent") && list.size() > 3) {
                        if (BwAddon.getTournament().hasPhase(list.get(2))) {
                            BwAddon.getTournament().getPhase(list.get(2)).setParentPhase(list.get(3));
                            commandSender.sendMessage("§aParent phase for phase §7" + list.get(2) + "§a is now §7" + list.get(3));
                        } else {
                            commandSender.sendMessage("§cThis phase has never been in this tournament:§7 " + list.get(2));
                        }
                        return true;
                    } else if (list.get(1).equalsIgnoreCase("teams") && list.size() > 3) {
                        if (BwAddon.getTournament().hasPhase(list.get(2))) {
                            BwAddon.getTournament().getPhase(list.get(2)).setTeams(Integer.parseInt(list.get(3)));
                            commandSender.sendMessage("§aMaximum teams number was set to §7" + list.get(3) + "§a for phase §7" + list.get(2));
                        } else {
                            commandSender.sendMessage("§cThis phase has never been in this tournament:§7 " + list.get(2));
                        }
                        return true;
                    } else if (list.get(1).equalsIgnoreCase("remove")) {
                        if (BwAddon.getTournament().hasPhase(list.get(2))) {
                            BwAddon.getTournament().removePhase(BwAddon.getTournament().getPhase(list.get(2)));
                            commandSender.sendMessage("§aThis phase was removed (note that you should update parents manually):§7 " + list.get(2));
                        } else {
                            commandSender.sendMessage("§cThis phase has never been in this tournament:§7 " + list.get(2));
                        }
                        return true;
                    } else if (list.get(1).equalsIgnoreCase("nophase")) {
                        if (BwAddon.getTournament().hasPhase(list.get(2))) {
                            BwAddon.getTournament().getPhase(list.get(2)).setParentPhase(null);
                            commandSender.sendMessage("§aNow phase don't have any parent:§7 " + list.get(2));
                        } else {
                            commandSender.sendMessage("§cThis phase has never been in this tournament:§7 " + list.get(2));
                        }
                        return true;
                    }
                } else if (list.get(0).equalsIgnoreCase("start")) {
                    BwAddon.getTournament().startTournament();
                    return true;
                } else if (list.get(0).equalsIgnoreCase("clear")) {
                    BwAddon.getTournament().getTeams().clear();
                    BwAddon.getTournament().getPhases().clear();
                    BwAddon.getTournament().setName("Tournament");
                    commandSender.sendMessage("§aTournament settings was removed and reverted!");
                    return true;
                } else if (list.get(0).equalsIgnoreCase("save")) {
                    BwAddon.getTournament().save();
                    commandSender.sendMessage("§aTournament saved!");
                    return true;
                } else if (list.get(0).equalsIgnoreCase("load")) {
                    BwAddon.getTournament().load();
                    commandSender.sendMessage("§aTournament loaded!");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void completeTab(List<String> completion, CommandSender commandSender, List<String> args) {
        if (args.size() == 1) {
            if (BwAddon.getTournament().isActive()) {
                completion.addAll(Arrays.asList("stop", "forceRoundLoad"));
            } else {
                completion.addAll(Arrays.asList("name", "addteam", "join", "leave", "removeteam", "start", "clear", "phase", "save", "load"));
            }
        } else if (!BwAddon.getTournament().isActive()) {
            if (args.get(0).equalsIgnoreCase("phase") && args.size() == 2) {
                completion.addAll(Arrays.asList("add", "addarena", "removearena", "parent", "teams", "remove", "noparent"));
            } else if (args.get(0).equalsIgnoreCase("phase") && args.size() == 3 && !args.get(1).equalsIgnoreCase("add")) {
                completion.addAll(BwAddon.getTournament().getPhases().stream().map(Phase::getCodeName).collect(Collectors.toList()));
            } else if ((args.get(0).equalsIgnoreCase("join") || args.get(0).equalsIgnoreCase("leave") || args.get(0).equalsIgnoreCase("remove")) && args.size() == 2) {
                completion.addAll(BwAddon.getTournament().getTeams().stream().map(TournamentTeam::getTeamName).collect(Collectors.toList()));
            } else if (args.get(0).equalsIgnoreCase("join") && args.size() == 3) {
                completion.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
            } else if (args.get(0).equalsIgnoreCase("leave") && args.size() == 3) {
                if (BwAddon.getTournament().getTeam(args.get(1)) != null) {
                    completion.addAll(BwAddon.getTournament().getTeam(args.get(1)).getPlayers().stream().map(uuid -> Bukkit.getOfflinePlayer(uuid).getName()).collect(Collectors.toList()));
                }
            } else if (args.get(0).equalsIgnoreCase("phase") && args.get(1).equalsIgnoreCase("parent") && args.size() == 4) {
                completion.addAll(BwAddon.getTournament().getPhases().stream().map(Phase::getCodeName).filter(str -> !str.equals(args.get(2))).collect(Collectors.toList()));
            } else if (args.get(0).equalsIgnoreCase("phase") && args.get(1).equalsIgnoreCase("addarena") && args.size() == 4) {
                completion.addAll(BedwarsAPI.getInstance().getGames().stream().map(Game::getName).collect(Collectors.toList()));
            } else if (args.get(0).equalsIgnoreCase("phase") && args.get(1).equalsIgnoreCase("removearena") && args.size() == 4) {
                if (BwAddon.getTournament().getPhase(args.get(2)) != null) {
                    completion.addAll(BwAddon.getTournament().getPhase(args.get(2)).getPossibleArenas());
                }
            } else if (args.get(0).equalsIgnoreCase("phase") && args.get(1).equalsIgnoreCase("teams") && args.size() == 4) {
                completion.addAll(Arrays.asList("2", "4", "8"));
            }
        }
    }
}
