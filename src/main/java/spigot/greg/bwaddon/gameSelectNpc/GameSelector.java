package spigot.greg.bwaddon.gameSelectNpc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import spigot.greg.bwaddon.BwAddon;

import static spigot.greg.bwaddon.I18n.*;

public class GameSelector implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            if (BwAddon.getTournament().isActive()) {
                BwAddon.getTournament().getRunningTournament().findGameFor((Player) sender);
            } else {
                mpr("offline").send(sender);
            }
        }
        return true;
    }
}
