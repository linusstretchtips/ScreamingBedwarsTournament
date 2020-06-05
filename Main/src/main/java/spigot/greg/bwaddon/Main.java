package spigot.greg.bwaddon;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.inventory.Options;
import spigot.greg.bwaddon.listeners.PlayerListener;

public final class Main extends JavaPlugin {
    private static  Main plugin;

    @Override
    public void onEnable() {
        new PlayerListener(this);
        plugin = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
