package xanth.ogsammaenr.xanthHelp.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.manager.TicketManager;
import xanth.ogsammaenr.xanthHelp.model.Ticket;
import xanth.ogsammaenr.xanthHelp.model.TicketStatus;
import xanth.ogsammaenr.xanthHelp.util.ItemBuilder;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminSupportMenu {
    private final XanthHelp plugin;
    private final TicketManager ticketManager;
    private final TicketStatus filter;
    private final int page;

    public AdminSupportMenu(XanthHelp plugin, TicketStatus filter, int page) {
        this.plugin = plugin;
        this.ticketManager = plugin.getTicketManager();
        this.filter = filter;
        this.page = page;
    }

    public void open(Player admin) {
        Inventory gui = Bukkit.createInventory(null, 54, "§cYardım Talepleri §7 " +
                ((filter == null) ? "ALL" : filter) + " " + page);

        // Seçilen filtreye göre ticketları getir
        try {
            List<Ticket> tickets = filter == null
                    ? ticketManager.getAllTickets()
                    : ticketManager.getTicketsByStatus(filter);

            int ticketsPerPage = 45;
            int start = page * ticketsPerPage;
            int end = Math.min(start + ticketsPerPage, tickets.size());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            for (int i = start; i < end; i++) {
                Ticket ticket = tickets.get(i);
                String playerName = plugin.getServer().getOfflinePlayer(ticket.getCreatorUUID()).getName();
                String displayStatus = ticket.getStatus().getDisplayName();
                String displayCategory = ticket.getCategory().getId();

                ItemStack item = new ItemBuilder(Material.PAPER)
                        .setName("§f" + ticket.getTicketId())
                        .addLore("§7Kategori: §e" + displayCategory)
                        .addLore("§7Oyuncu: §b" + playerName)
                        .addLore("§7Tarih: §f" + ticket.getCreatedAt().format(formatter))
                        .addLore("§7Açıklama: §f" + ticket.getDescription())
                        .addLore("§7Durum: §f" + displayStatus)
                        .setNBT("admin_tab", ticket.getTicketId())
                        .build();

                ticket.toString();

                gui.setItem(i - start, item); // 0–44 slot arası
            }

            // Sekmeler (alt satır)
            gui.setItem(47, new ItemBuilder(Material.BOOK).setName("§eTümü").setNBT("admin_tab", "ALL").build());
            gui.setItem(48, new ItemBuilder(Material.LIME_DYE).setName("§aAçık").setNBT("admin_tab", "OPEN").build());
            gui.setItem(49, new ItemBuilder(Material.YELLOW_DYE).setName("§eİşlemde").setNBT("admin_tab", "IN_PROGRESS").build());
            gui.setItem(50, new ItemBuilder(Material.GRAY_DYE).setName("§7Çözüldü").setNBT("admin_tab", "RESOLVED").build());
            gui.setItem(51, new ItemBuilder(Material.RED_DYE).setName("§cİptal Edildi").setNBT("admin_tab", "CANCELED").build());

            // Sonraki sayfa
            if (tickets.size() > end) {
                gui.setItem(53, new ItemBuilder(Material.ARROW).setName("§aSonraki Sayfa").setNBT("admin_tab", "NEXT_PAGE").build());
            }

            // Geri sayfa (isteğe bağlı)
            if (page > 0) {
                gui.setItem(45, new ItemBuilder(Material.ARROW).setName("§cÖnceki Sayfa").setNBT("admin_tab", "PREVIOUS_PAGE").build());
            }
        } catch (SQLException e) {
            admin.sendMessage("§cTicket verileri alınırken bir hata oluştu.");
            e.printStackTrace();
        }

        admin.openInventory(gui);
    }
}
