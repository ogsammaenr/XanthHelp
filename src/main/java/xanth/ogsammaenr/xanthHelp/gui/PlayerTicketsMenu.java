package xanth.ogsammaenr.xanthHelp.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.manager.TicketManager;
import xanth.ogsammaenr.xanthHelp.model.Ticket;
import xanth.ogsammaenr.xanthHelp.util.ItemBuilder;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PlayerTicketsMenu {
    private final XanthHelp plugin;
    private final TicketManager ticketManager;
    private final int page;

    public PlayerTicketsMenu(XanthHelp plugin, int page) {
        this.plugin = plugin;
        this.ticketManager = plugin.getTicketManager();
        this.page = page;
    }

    public void open(Player player) {
        List<Ticket> tickets;
        try {
            tickets = ticketManager.getTicketsByCreator(player.getUniqueId());
        } catch (SQLException e) {
            player.sendMessage("§cTicketlar Yüklenirken Bir Hata Oluştu");
            e.printStackTrace();
            return;
        }
        Inventory gui = Bukkit.createInventory(null, 54, "§aDestek Taleplerim§7 " + page);

        int ticketsPerPage = 45;
        int start = page * ticketsPerPage;
        int end = Math.min(start + ticketsPerPage, tickets.size());

        for (int i = start; i < end; i++) {
            Ticket ticket = tickets.get(i);

            ItemStack item = new ItemBuilder(Material.PAPER)
                    .setName("§e#" + ticket.getTicketId())
                    .addLore("§7Kategori: §f" + ticket.getCategory().getId())
                    .addLore("§7Durum: " + ticket.getStatus().name())
                    .addLore("§7Tarih: §f" + ticket.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                    .addLore(ticket.getAssignedStaffUUID() != null ? "§7Yetkili: §f" + Bukkit.getOfflinePlayer(ticket.getAssignedStaffUUID()).getName() : "§7Yetkili: §cYok")
                    .addLore("")
                    .addLore("§e» Detayları görmek için tıkla")
                    .setNBT("player_tickets_tab", ticket.getTicketId())
                    .build();
            gui.setItem(i - start, item); // 0–44 slot arası
        }

        // Sonraki sayfa
        if (tickets.size() > end) {
            gui.setItem(53, new ItemBuilder(Material.ARROW)
                    .setName("§aSonraki Sayfa")
                    .setNBT("player_tickets_tab", "next_page")
                    .build());
        }

        // Geri sayfa
        if (page > 0) {
            gui.setItem(45, new ItemBuilder(Material.ARROW)
                    .setName("§cÖnceki Sayfa")
                    .setNBT("player_tickets_tab", "previous_page")
                    .build());
        }

        player.openInventory(gui);
    }
}
