package xanth.ogsammaenr.xanthHelp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.gui.AdminSupportMenu;
import xanth.ogsammaenr.xanthHelp.model.TicketStatus;

public class AdminHelpCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Bu komudu sadece oyuncular kullanabilir");
            return false;
        }
        Player player = (Player) commandSender;
        XanthHelp plugin = XanthHelp.getInstance();

        new AdminSupportMenu(plugin, TicketStatus.OPEN, 1).open(player);
        return true;
    }
}
