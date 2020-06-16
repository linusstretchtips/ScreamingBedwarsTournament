package spigot.greg.bwaddon;

import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.bedwars.Main;
import spigot.greg.bwaddon.gameSelectNpc.GameSelector;

public final class BwAddon extends JavaPlugin {
    private static BwAddon plugin;
    private static Tournament tournament;

    @Override
    public void onEnable() {
        plugin = this;
        tournament = new Tournament();
        tournament.load();
        getServer().getPluginManager().registerEvents(new BedwarsListener(), this);
        new AdminCommands();
        getCommand("jointournamentround").setExecutor(new GameSelector());
        I18n.load(this, Main.getConfigurator().config.getString("locale"));
    }

    @Override
    public void onDisable() {
        Main.getCommands().remove("tournament");
    }

    public static BwAddon getInstance() {
        return plugin;
    }

    public static Tournament getTournament() {
        return tournament;
    }


}
