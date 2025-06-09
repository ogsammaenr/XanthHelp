package xanth.ogsammaenr.xanthHelp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.manager.ChannelManager;
import xanth.ogsammaenr.xanthHelp.manager.TicketManager;
import xanth.ogsammaenr.xanthHelp.model.Ticket;
import xanth.ogsammaenr.xanthHelp.model.TicketStatus;

import java.util.Optional;

public class TicketChatCommand implements CommandExecutor {
    private final XanthHelp plugin;
    private final TicketManager ticketManager;
    private final ChannelManager channelManager;

    public TicketChatCommand(XanthHelp plugin) {
        this.plugin = plugin;
        this.ticketManager = plugin.getTicketManager();
        this.channelManager = plugin.getChannelManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Bu komut sadece oyuncular tarafından kullanılabilir.");
            return true;
        }

        Optional<Ticket> optional = ticketManager.getActiveTicketOf(player.getUniqueId());

        if (optional.isEmpty()) {
            player.sendMessage("§cAktif bir ticket'ınız bulunmamaktadır.");
            return true;
        }

        Ticket ticket = optional.get();

        if (ticket.getStatus() != TicketStatus.IN_PROGRESS) {
            player.sendMessage("§cBu ticket şu anda aktif bir sohbete açık değil.");
            return true;
        }

        boolean joined = channelManager.joinChannel(ticket.getTicketId(), player.getUniqueId());

        if (joined) {
            player.sendMessage("§aTicket sohbet kanalına katıldınız.");
        } else {
            player.sendMessage("§cZaten bu ticket sohbet kanalına katılmışsınız.");
        }

        return true;
    }

    
}
