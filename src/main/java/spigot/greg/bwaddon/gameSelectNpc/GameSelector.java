package spigot.greg.bwaddon.gameSelectNpc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import spigot.greg.bwaddon.BwAddon;

public class GameSelector implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (BwAddon.getTournament().isActive() && sender instanceof Player) {
            BwAddon.getTournament().getRunningTournament().findGameFor((Player) sender);
        }
        return true;
    }
}
