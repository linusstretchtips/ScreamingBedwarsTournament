package spigot.greg.bwaddon.listeners;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import org.bukkit.Bukkit;
import org.screamingsandals.bedwars.api.BedwarsAPI;

public class PlayerListener implements Listener {
    spigot.greg.bwaddon.Main plugin;
    private BedwarsAPI api;

    public PlayerListener(spigot.greg.bwaddon.Main plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent evt) {

        api = BedwarsAPI.getInstance();
        if (!api.isPlayerPlayingAnyGame(evt.getPlayer())) return;

        if (evt.getItemDrop().getItemStack().getType() == Material.WOOD_SWORD) {
            evt.setCancelled(true);
            evt.getPlayer().getInventory().remove(evt.getItemDrop().getItemStack());
            if (evt.getItemDrop().getItemStack().getType() == Material.STONE_SWORD) {
                evt.setCancelled(true);
                evt.getPlayer().getInventory().remove(evt.getItemDrop().getItemStack());
                if (evt.getItemDrop().getItemStack().getType() == Material.GOLD_SWORD) {
                    evt.setCancelled(true);
                    evt.getPlayer().getInventory().remove(evt.getItemDrop().getItemStack());
                    if (evt.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD) {
                        evt.setCancelled(true);
                        evt.getPlayer().getInventory().remove(evt.getItemDrop().getItemStack());
                        if (evt.getItemDrop().getItemStack().getType() == Material.WOOD_PICKAXE) {
                            evt.setCancelled(true);
                            evt.getPlayer().getInventory().remove(evt.getItemDrop().getItemStack());
                            if (evt.getItemDrop().getItemStack().getType() == Material.STONE_PICKAXE) {
                                evt.setCancelled(true);
                                evt.getPlayer().getInventory().remove(evt.getItemDrop().getItemStack());
                                if (evt.getItemDrop().getItemStack().getType() == Material.GOLD_PICKAXE) {
                                    evt.setCancelled(true);
                                    evt.getPlayer().getInventory().remove(evt.getItemDrop().getItemStack());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
