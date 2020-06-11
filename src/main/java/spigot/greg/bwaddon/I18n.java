package spigot.greg.bwaddon;

import org.bukkit.plugin.Plugin;
import org.screamingsandals.bedwars.lib.lang.BukkitTranslateContainer;
import org.screamingsandals.bedwars.lib.lang.I;
import org.screamingsandals.bedwars.lib.lang.ITranslateContainer;
import org.screamingsandals.bedwars.lib.lang.Message;

import java.io.File;

public class I18n {
    public static final String BASE_LANG_CODE = I.BASE_LANG_CODE;

    protected static String locale = I.BASE_LANG_CODE;
    protected static ITranslateContainer customContainer;
    protected static ITranslateContainer mainContainer;
    protected static ITranslateContainer fallbackContainer;

    public static String i18n(String key) {
        return i18n(key, null, true);
    }

    public static String i18nonly(String key) {
        return i18n(key, null, false);
    }

    public static String i18n(String key, boolean prefix) {
        return i18n(key, null, prefix);
    }

    public static String i18n(String key, String defaultK) {
        return i18n(key, defaultK, true);
    }

    public static String i18nonly(String key, String defaultK) {
        return i18n(key, defaultK, false);
    }

    public static String i18n(String key, String def, boolean prefix) {
        if (prefix) {
            return customContainer.translateWithPrefix(key, def);
        } else {
            return customContainer.translate(key, def);
        }
    }

    public static Message mpr() {
        return m(null, null, true);
    }

    public static Message mpr(String key) {
        return m(key, null, true);
    }

    public static Message mpr(String key, String def) {
        return m(key, def, true);
    }

    public static Message m() {
        return m(null, null, false);
    }

    public static Message m(String key) {
        return m(key, null, false);
    }

    public static Message m(String key, boolean prefix) {
        return m(key, null, prefix);
    }

    public static Message m(String key, String def) {
        return m(key, def, false);
    }

    public static Message m(String key, String def, boolean prefix) {
        return new Message(key, customContainer, def, prefix);
    }

    public static void load(Plugin plugin, String loc) {
        if (loc != null && !"".equals(loc.trim())) {
            locale = loc;
        }

        if (!BASE_LANG_CODE.equals(locale)) {
            fallbackContainer = new BukkitTranslateContainer(BASE_LANG_CODE, plugin);
        }

        mainContainer = new BukkitTranslateContainer(locale, plugin, fallbackContainer);

        customContainer = new BukkitTranslateContainer(new File(plugin.getDataFolder().toString(), "languages"), locale, mainContainer);

        plugin.getLogger().info(
                "Successfully loaded messages for " + plugin.getName() + "! Language: " + customContainer.getLanguage());
    }
}
