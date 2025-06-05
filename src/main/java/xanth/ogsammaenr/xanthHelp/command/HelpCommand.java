package xanth.ogsammaenr.xanthHelp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.gui.MainMenu;

public class HelpCommand implements CommandExecutor {
    private XanthHelp plugin;

    private MainMenu mainMenu;

    public HelpCommand(XanthHelp plugin) {
        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            plugin.getLogger().warning("This command can only be executed by a player!");
            return false;
        }
        Player player = (Player) commandSender;

        new MainMenu(plugin).open(player);

        return true;
    }
}
