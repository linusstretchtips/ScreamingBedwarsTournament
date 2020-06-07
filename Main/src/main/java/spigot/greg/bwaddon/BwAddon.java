package spigot.greg.bwaddon;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.inventory.Options;
import org.screamingsandals.simpleinventories.listeners.InventoryListener;
import spigot.greg.bwaddon.gameSelectNpc.customShop;
import org.bukkit.permissions.*;
import org.bukkit.plugin.PluginManager;

public final class BwAddon extends JavaPlugin {
    private static  BwAddon plugin;
    
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
    
    public BwAddon getInstance()
    {
    	return plugin;
    }
    
    
    
}
