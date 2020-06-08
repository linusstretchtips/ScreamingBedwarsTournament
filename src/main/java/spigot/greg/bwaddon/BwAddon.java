package spigot.greg.bwaddon;

import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.simpleinventories.listeners.InventoryListener;
import spigot.greg.bwaddon.gameSelectNpc.customShop;

public final class BwAddon extends JavaPlugin {
    private static BwAddon plugin;

    @Override
    public void onEnable() {
        plugin = this;
        InventoryListener.init(this);
        getCommand("customshopgui").setExecutor(new customShop(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public BwAddon getInstance() {
        return plugin;
    }


}
