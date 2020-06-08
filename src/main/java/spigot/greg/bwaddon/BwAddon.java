package spigot.greg.bwaddon;

import org.bukkit.plugin.java.JavaPlugin;
import spigot.greg.bwaddon.gameSelectNpc.GameSelector;

public final class BwAddon extends JavaPlugin {
    private static BwAddon plugin;
    private static Tournament tournament;

    @Override
    public void onEnable() {
        plugin = this;
        tournament = new Tournament();
        getServer().getPluginManager().registerEvents(new BedwarsListener(), this);
        new AdminCommands();
        getCommand("jointournamentround").setExecutor(new GameSelector());
    }

    @Override
    public void onDisable() {

    }

    public static BwAddon getInstance() {
        return plugin;
    }

    public static Tournament getTournament() {
        return tournament;
    }


}
