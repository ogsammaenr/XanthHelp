package xanth.ogsammaenr.xanthHelpDevelop.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.gui.ConfirmationMenu;
import xanth.ogsammaenr.xanthHelpDevelop.manager.TicketInputManager;
import xanth.ogsammaenr.xanthHelpDevelop.manager.TicketManager;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategory;

import java.util.UUID;

public class ChatInputListener implements Listener {
    private final XanthHelp plugin;
    private final TicketInputManager inputManager;
    private final TicketManager ticketManager;

    public ChatInputListener() {
        this.plugin = XanthHelp.getInstance();
        this.inputManager = plugin.getTicketInputManager();
        this.ticketManager = plugin.getTicketManager();

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!inputManager.isWaiting(playerId)) return;

        String message = event.getMessage().trim();
        if (message.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "Açıklama Boş Olamaz");
            return;
        }
        if (message.equals("quit")) {
            player.sendMessage("Ticket iptal Edildi");
        }


        TicketCategory category = inputManager.consumeCategory(event.getPlayer());

        Bukkit.getScheduler().runTask(plugin, () -> {
            new ConfirmationMenu(message, category).open(player, "Create Ticket");
        });

    }
}
