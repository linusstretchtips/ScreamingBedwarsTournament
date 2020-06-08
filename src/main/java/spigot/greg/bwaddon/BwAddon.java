package spigot.greg.bwaddon;

import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.simpleinventories.listeners.InventoryListener;
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

        // fuck this shit I'm out
        InventoryListener.init(this);
        getCommand("customshopgui").setExecutor(new GameSelector(this));
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
