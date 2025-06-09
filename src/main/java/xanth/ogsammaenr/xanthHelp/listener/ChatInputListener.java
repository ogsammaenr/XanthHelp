package xanth.ogsammaenr.xanthHelp.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.manager.ChannelManager;
import xanth.ogsammaenr.xanthHelp.manager.TicketManager;
import xanth.ogsammaenr.xanthHelp.model.Category;
import xanth.ogsammaenr.xanthHelp.model.Ticket;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class ChatInputListener implements Listener {
    private final XanthHelp plugin;
    private final TicketManager ticketManager;
    private final ChannelManager channelManager;

    // Açıklama bekleyen oyuncular ve seçtikleri kategori
    private final Map<UUID, Category> waitingPlayers;
    private final Map<UUID, Long> waitingPlayersStartTime;

    public ChatInputListener(XanthHelp plugin) {
        this.plugin = plugin;
        this.ticketManager = plugin.getTicketManager();
        this.channelManager = plugin.getChannelManager();
        this.waitingPlayers = new HashMap<>();
        this.waitingPlayersStartTime = new HashMap<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                Iterator<Map.Entry<UUID, Long>> iterator = waitingPlayersStartTime.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<UUID, Long> entry = iterator.next();
                    if (now - entry.getValue() > 15_000) {  // 15 saniye geçti mi?
                        UUID playerUUID = entry.getKey();
                        waitingPlayers.remove(playerUUID);
                        iterator.remove();

                        Player player = Bukkit.getPlayer(playerUUID);
                        if (player != null && player.isOnline()) {
                            player.sendMessage(ChatColor.RED + "§cZaman aşımına uğradı. Destek talebi oluşturma işlemi iptal edildi.");
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        ///     Ticket Açıklaması Kontrolü
        if (waitingPlayers.containsKey(playerUUID)) {
            event.setCancelled(true); // Mesajı global sohbette gösterme

            String description = event.getMessage();
            Category category = waitingPlayers.get(playerUUID);

            if (description.equalsIgnoreCase("iptal")) {
                waitingPlayers.remove(playerUUID);
                waitingPlayersStartTime.remove(playerUUID);
                player.sendMessage("§cDestek talebi iptal edildi.");
                return;
            }

            // Ticket ID oluştur (örneğin UUID.randomUUID ile)
            int nextNumber;
            try {
                nextNumber = plugin.getTicketManager().getNextTicketNumber();
            } catch (SQLException e) {
                e.printStackTrace();

                waitingPlayers.remove(playerUUID);
                waitingPlayersStartTime.remove(playerUUID);
                player.sendMessage(ChatColor.RED + "Bir hata oluştu, lütfen daha sonra tekrar deneyin.");
                return;
            }
            String ticketId = "TCK-" + nextNumber;

            Ticket ticket = new Ticket(
                    ticketId,
                    playerUUID,
                    category,
                    description,
                    LocalDateTime.now()
            );

            try {
                ticketManager.createTicket(ticket);
                player.sendMessage("§aBaşarıyla destek talebiniz oluşturuldu! Ticket ID: " + ticketId);
                player.sendMessage(ticket.toString());
            } catch (SQLException e) {
                e.printStackTrace();
                player.sendMessage("§cDestek talebi oluşturulurken bir hata oluştu. Lütfen daha sonra tekrar deneyin.");
            }

            // Oyuncuyu waitingPlayers'dan çıkar
            waitingPlayers.remove(playerUUID);
            waitingPlayersStartTime.remove(playerUUID);
        }

        ///     sohbet kanalı kontrolü
        String ticketId = channelManager.getChannelOf(playerUUID);
        if (ticketId != null) {
            Set<Player> receivers = channelManager.getParticipants(ticketId).stream()
                    .map(Bukkit::getPlayer)
                    .filter(p -> p != null && p.isOnline())
                    .collect(java.util.stream.Collectors.toSet());

            event.getRecipients().clear();
            event.getRecipients().addAll(receivers);

            // Prefix ile mesajı biçimlendir (isteğe bağlı)
            event.setFormat("§7[" + ticketId + "] §b" + player.getName() + "§f: " + event.getMessage());
        }
    }

    // Bir oyuncuyu açıklama yazması için beklemeye alır
    public void startWaitingForDescription(UUID playerUUID, Category category) {
        waitingPlayers.put(playerUUID, category);
        waitingPlayersStartTime.put(playerUUID, System.currentTimeMillis());

    }
}
