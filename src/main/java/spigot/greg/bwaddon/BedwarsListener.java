package spigot.greg.bwaddon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndingEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameTickEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerJoinEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPostRebuildingEvent;

public class BedwarsListener implements Listener {
    @EventHandler
    public void onBWEnable(PluginEnableEvent event) {
        if (event.getPlugin().getName().equals("BedWars")) {
            new AdminCommands(); // re register commands
        }
    }

    @EventHandler
    public void onPlayerJoinBedwars(BedwarsPlayerJoinEvent event) {
        if (event.isCancelled()) {
            return;
        }

    }

    @EventHandler
    public void onGameEnds(BedwarsGameEndingEvent event) {

    }

    @EventHandler
    public void onGameTick(BedwarsGameTickEvent event) {

    }

    @EventHandler
    public void afterRebuild(BedwarsPostRebuildingEvent event) {

    }
}
