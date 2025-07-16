package xanth.ogsammaenr.xanthHelpDevelop;

import org.bukkit.plugin.java.JavaPlugin;

public final class XanthHelp extends JavaPlugin {
    private static XanthHelp instance;

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static XanthHelp getInstance() {
        return instance;
    }
}
