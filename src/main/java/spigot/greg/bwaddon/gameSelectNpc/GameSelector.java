package spigot.greg.bwaddon.gameSelectNpc;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.events.PostActionEvent;
import org.screamingsandals.simpleinventories.inventory.Options;
import org.screamingsandals.simpleinventories.utils.MapReader;


public class GameSelector implements CommandExecutor, Listener {

    public SimpleInventories format;

    public GameSelector(spigot.greg.bwaddon.BwAddon plugin) {
        Options options = new Options(plugin.getInstance());

        ItemStack backItem = new ItemStack(Material.BARRIER);
        ItemMeta backItemMeta = backItem.getItemMeta();
        backItemMeta.setDisplayName("back");
        backItem.setItemMeta(backItemMeta);
        options.setBackItem(backItem);

        ItemStack pageBackItem = new ItemStack(Material.ARROW);
        ItemMeta pageBackItemMeta = backItem.getItemMeta();
        pageBackItemMeta.setDisplayName("previous back");
        pageBackItem.setItemMeta(pageBackItemMeta);
        options.setPageBackItem(pageBackItem);

        ItemStack pageForwardItem = new ItemStack(Material.ARROW);
        ItemMeta pageForwardItemMeta = backItem.getItemMeta();
        pageForwardItemMeta.setDisplayName("next page");
        pageForwardItem.setItemMeta(pageForwardItemMeta);
        options.setPageForwardItem(pageForwardItem);

        ItemStack cosmeticItem = new ItemStack(Material.AIR);
        options.setCosmeticItem(cosmeticItem);

        options.setRows(3);
        options.setRender_actual_rows(3);
        options.setRender_offset(0);
        options.setShowPageNumber(false);

        options.setPrefix("§cBed§fwars §eTournament");
        options.setAnimationsEnabled(true);


        format = new SimpleInventories(options);
        try {
            format.loadFromDataFolder(plugin.getDataFolder(), "tournament.groovy");
        } catch (java.lang.Exception io) {
            Bukkit.getLogger().info("could not load tournament.groovy");
        }
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin.getInstance());
        format.generateData(); // now generate gui

    }

    @EventHandler
    public void onPostAction(PostActionEvent event) {
        if (event.getFormat() != format) {
            return; // you should check if the format is yours
        }

        if (event.isCancelled()) {
            return; // you should stop working when event is cancelled
        }

        event.getItem().getProperties().forEach(itemProperty -> {
            if (itemProperty.getPropertyName().equalsIgnoreCase("tournament_join")) {
                BedwarsAPI api = BedwarsAPI.getInstance();
                MapReader reader = itemProperty.getReader(event.getPlayer(), event.getItem());
                Game game = api.getGameByName(reader.getString("game"));
                if (game != null) {
                    if (event.getPlayer().hasPermission(reader.getString("permission"))) {
                        game.joinToGame(event.getPlayer());
                    } else {
                        event.getPlayer().sendMessage("§c&LHEY!, §cSorry but you Havnt got Permission to do this Yet!");
                    }
                }
            }
        });

    }

    public void show(Player player) {
        format.openForPlayer(player);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("customshopgui")) {
            show((Player) sender);
        }

        return true;
    }
}
