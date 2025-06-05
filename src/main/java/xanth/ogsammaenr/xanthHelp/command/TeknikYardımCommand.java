package xanth.ogsammaenr.xanthHelp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.gui.CategoryMenu;

public class TeknikYardımCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        XanthHelp plugin = XanthHelp.getInstance();

        if (!(commandSender instanceof Player)) {
            plugin.getLogger().warning("This command can only be executed by a player!");
            return false;
        }
        Player player = (Player) commandSender;

        new CategoryMenu(plugin).open(player, "TEKNIK");
        return true;
    }
}
