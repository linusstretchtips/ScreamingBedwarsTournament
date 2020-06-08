package spigot.greg.bwaddon.gameSelectNpc;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.events.GenerateItemEvent;
import org.screamingsandals.simpleinventories.events.PostActionEvent;
import org.screamingsandals.simpleinventories.events.PreActionEvent;
import org.screamingsandals.simpleinventories.events.ShopTransactionEvent;
import org.screamingsandals.simpleinventories.inventory.Options;
import org.screamingsandals.simpleinventories.item.ItemProperty;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;
import org.screamingsandals.simpleinventories.utils.MapReader;
import org.bukkit.Material;
import org.screamingsandals.simpleinventories.inventory.Options;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class customShop implements CommandExecutor, Listener  {
	
	public SimpleInventories format;
	
	public customShop(spigot.greg.bwaddon.BwAddon plugin)
	{
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
			options.setRender_header_start(0);
			options.setRender_footer_start(45);
			options.setItems_on_row(9);
			options.setShowPageNumber(false);
			options.setInventoryType(InventoryType.valueOf("CHEST"));
			
			options.setPrefix("§cBed§fwars §eTournament");
			options.setGenericShop(true);
			options.setGenericShopPriceTypeRequired(true);
			options.setAnimationsEnabled(true);
			
			
			
			format = new SimpleInventories(options);
			try {
	        format.loadFromDataFolder(plugin.getDataFolder(), "Tournament.yml");
			}
			catch(java.lang.Exception io)
			{
				Bukkit.getLogger().info("could not load Tournament.yml");
			}
	        Bukkit.getServer().getPluginManager().registerEvents(this, plugin.getInstance());
	        format.generateData(); // now generate gui
	        
}
	@EventHandler
    public void onGeneratingItem(GenerateItemEvent event) {
        if (event.getFormat() != format) {
            return; // you should check if the format is yours
        }

        // here do some stuff on generating item with creator
    }
	  @EventHandler
	    public void onPreAction(PreActionEvent event) {
	        if (event.getFormat() != format) {
	            return; // you should check if the format is yours
	        }

	        if (event.isCancelled()) {
	            return; // you should stop working when event is cancelled
	        }

	    }
	  @EventHandler
	    public void onPostAction(PostActionEvent event) {
	        if (event.getFormat() != format) {
	            return; // you should check if the format is yours
	        }

	        if (event.isCancelled()) {
	            return; // you should stop working when event is cancelled
	        }

	        // here you should make do some stuff when player clicks to item
	        
	        

	    }
	  
	  public void show(Player player) {
	        format.openForPlayer(player);
	    }
	  
	  @Override
	  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	  {
	  if(commandLabel.equalsIgnoreCase("customshopgui")){
		  show((Player)sender);
	  }
	  
	  return true;
	  }
	   
	  @EventHandler
		public void onShopTransaction(ShopTransactionEvent event) {
			
			if(event.getStack().getItemMeta().getDisplayName().contains("Game E1") && event.getPlayer().hasPermission("tournament.g1team1to4"))
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sudo " + event.getPlayer().getName() + " bw join Icey");
			if(event.getStack().getItemMeta().getDisplayName().contains("Game E2") && event.getPlayer().hasPermission("tournament.g1team5to8"))
				    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sudo " + event.getPlayer().getName() + " bw join Icey2");
		    if(event.getStack().getItemMeta().getDisplayName().contains("Game E3") && event.getPlayer().hasPermission("tournament.g1team9to12"))
				    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sudo " + event.getPlayer().getName() + " bw join Icey3");
		    if(event.getStack().getItemMeta().getDisplayName().contains("Game E4") && event.getPlayer().hasPermission("tournament.g1team13to16"))
		    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sudo " + event.getPlayer().getName() + " bw join Icey4");
		    if(event.getStack().getItemMeta().getDisplayName().contains("Game E5") && event.getPlayer().hasPermission("tournament.g1team17to20"))
				    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sudo " + event.getPlayer().getName() + " bw join Icey5");
		    if(event.getStack().getItemMeta().getDisplayName().contains("Game E6") && event.getPlayer().hasPermission("tournament.g1team21to24"))
				    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sudo " + event.getPlayer().getName() + " bw join Icey6");
		    if(event.getStack().getItemMeta().getDisplayName().contains("Game E7") && event.getPlayer().hasPermission("tournament.g1team25to28"))
				    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sudo " + event.getPlayer().getName() + " bw join Icey7");
		    if(event.getStack().getItemMeta().getDisplayName().contains("Game E8") && event.getPlayer().hasPermission("tournament.g1team29to32"))
		    	    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sudo " + event.getPlayer().getName() + " bw join Icey8");
			else if(event.getStack().getItemMeta().getDisplayName().contains("Game F1") && event.getPlayer().hasPermission("tournament.g2team1to2"))
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sudo " + event.getPlayer().getName() + " bw join Village");
			     if(event.getStack().getItemMeta().getDisplayName().contains("Game F2") && event.getPlayer().hasPermission("tournament.g2team3to4"))
			 	 	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sudo " + event.getPlayer().getName() + " bw join Village2");
			else if(event.getStack().getItemMeta().getDisplayName().contains("FINALS") && event.getPlayer().hasPermission("tournament.game3"))
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sudo " + event.getPlayer().getName() + " bw join Deserted");
			else {
				event.getPlayer().sendMessage("§c&LHEY!, §cSorry but you Havnt got Permission to do this Yet!");
			}
}
}
