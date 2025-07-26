package xanth.ogsammaenr.xanthHelpDevelop.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.gui.MainMenu;
import xanth.ogsammaenr.xanthHelpDevelop.gui.TicketCategoriesMenu;
import xanth.ogsammaenr.xanthHelpDevelop.gui.TicketsMenu;
import xanth.ogsammaenr.xanthHelpDevelop.manager.TicketCategoryManager;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketStatus;

public class HelpCommand implements CommandExecutor {
    private final XanthHelp plugin;
    private final TicketCategoryManager catManager;

    public HelpCommand() {
        plugin = XanthHelp.getInstance();
        catManager = plugin.getTicketCategoryManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player!");
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            new MainMenu().open(player);
            return true;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("category")) {
                new TicketCategoriesMenu().open(player, catManager.getCategoryTypeById(args[1]));
                return true;
            }
            if (args[0].equalsIgnoreCase("tickets")) {
                if (args[1].equalsIgnoreCase("all")) {
                    new TicketsMenu().open(player, 0, null, null);
                    return true;
                }
                new TicketsMenu().open(player, 0, null, TicketStatus.valueOf(args[1]));
                return true;
            }
        }

        return false;
    }
}
