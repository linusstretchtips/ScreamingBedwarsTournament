package spigot.greg.bwaddon;

import org.bukkit.command.CommandSender;
import org.screamingsandals.bedwars.commands.BaseCommand;

import java.util.Arrays;
import java.util.List;

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
            commandSender.sendMessage("/bw tournament phase teams <teams> - Sets number of teams per game");
            commandSender.sendMessage("/bw tournament start - Starts the tournament");
            commandSender.sendMessage("/bw tournament stop - Stops the tournament");
            commandSender.sendMessage("/bw tournament clear - Clears tournament settings");
            commandSender.sendMessage("TODO: ability to save tournament for restoring it later");
        } else if (list.size() >= 1) {
            if (list.get(0).equalsIgnoreCase("name")) {

            } else if (list.get(0).equalsIgnoreCase("addteam")) {

            } else if (list.get(0).equalsIgnoreCase("join")) {

            } else if (list.get(0).equalsIgnoreCase("leave")) {

            } else if (list.get(0).equalsIgnoreCase("removeteam")) {

            } else if (list.get(0).equalsIgnoreCase("phase") && list.size() > 1) {
                if (list.get(1).equalsIgnoreCase("add")) {

                } else if (list.get(1).equalsIgnoreCase("addarena")) {

                } else if (list.get(1).equalsIgnoreCase("removearena")) {

                } else if (list.get(1).equalsIgnoreCase("parent")) {

                } else if (list.get(1).equalsIgnoreCase("teams")) {

                }
            } else if (list.get(0).equalsIgnoreCase("start")) {

            } else if (list.get(0).equalsIgnoreCase("stop")) {

            } else if (list.get(0).equalsIgnoreCase("clear")) {

            }
        }
        return false;
    }

    @Override
    public void completeTab(List<String> completion, CommandSender commandSender, List<String> args) {
        if (args.size() == 1) {
            if (BwAddon.getTournament().isActive()) {
                completion.addAll(Arrays.asList("stop"));
            } else {
                completion.addAll(Arrays.asList("name", "addteam", "join", "leave", "removeteam", "start", "clear", "phase"));
            }
        } else if (!BwAddon.getTournament().isActive() && args.size() > 1) {
            if (args.get(0).equalsIgnoreCase("phase")) {
                completion.addAll(Arrays.asList("add", "addarena", "removearena", "parent", "teams"));
            }
        }
    }
}
