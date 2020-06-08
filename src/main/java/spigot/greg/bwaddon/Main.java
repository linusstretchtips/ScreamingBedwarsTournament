package spigot.greg.bwaddon;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.inventory.Options;
import spigot.greg.bwaddon.listeners.PlayerListener;

import java.io.File;
import java.util.logging.Logger;

public final class Main extends JavaPlugin implements Listener {
    private static  Main plugin;

    Logger Logger = Bukkit.getLogger();

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
