package xanth.ogsammaenr.xanthHelp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.gui.PlayerTicketsMenu;

public class PlayerTicketsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be executed by a player!");
            return false;
        }

        Player player = (Player) commandSender;
        XanthHelp plugin = XanthHelp.getInstance();

        new PlayerTicketsMenu(plugin, 0).open(player);
        return true;

    }
}
