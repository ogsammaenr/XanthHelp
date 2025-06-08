package xanth.ogsammaenr.xanthHelp.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import xanth.ogsammaenr.xanthHelp.model.Ticket;
import xanth.ogsammaenr.xanthHelp.util.ItemBuilder;

import java.time.format.DateTimeFormatter;

public class TicketDetailMenu {
    private final Ticket ticket;
    private final Player viewer;

    public TicketDetailMenu(Ticket ticket, Player viewer) {
        this.ticket = ticket;
        this.viewer = viewer;
    }

    public void open() {
        Inventory inv = Bukkit.createInventory(null, 36, "Ticket Detayı " + ticket.getTicketId());

        String yetkili = (ticket.getAssignedStaffUUID() == null)
                ? "Ticket Henüz Sahiplenilmedi"
                : Bukkit.getOfflinePlayer(ticket.getAssignedStaffUUID()).getName();
        String sahiplenilen_tarih = (ticket.getAssignedAt() == null)
                ? "Ticket Henüz Sahiplenilmedi"
                : ticket.getAssignedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        String cozulen_tarih = (ticket.getResolvedAt() == null)
                ? "Ticket Henüz Sahiplenilmedi"
                : ticket.getResolvedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));


        inv.setItem(11, new ItemBuilder(Material.BOOK).setName("§eKategori").addLore(ticket.getCategory().getId()).build());
        inv.setItem(12, new ItemBuilder(Material.PAPER).setName("§bAçıklama").addLore(ticket.getDescription()).build());
        inv.setItem(13, new ItemBuilder(Material.CLOCK).setName("§fTarih").addLore(ticket.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))).build());
        inv.setItem(14, new ItemBuilder(Material.NAME_TAG).setName("§aOyuncu").addLore(Bukkit.getPlayer(ticket.getCreatorUUID()).getName()).build());
        inv.setItem(15, new ItemBuilder(Material.MAP).setName("§6Durum").addLore(ticket.getStatus().name()).build());
        inv.setItem(21, new ItemBuilder(Material.PLAYER_HEAD).setName("§aTicketı Alan Yetkili").addLore(yetkili).build());
        inv.setItem(22, new ItemBuilder(Material.CLOCK).setName("§aTicketın Alındığı Tarih").addLore(sahiplenilen_tarih).build());
        inv.setItem(23, new ItemBuilder(Material.COMPARATOR).setName("§aTicketın Çözüldüğü Tarih").addLore(cozulen_tarih).build());

        inv.setItem(27, new ItemBuilder(Material.ARROW)
                .setName("§fGeri Dön")
                .setNBT("ticket_detail_tab", "previous_page")
                .build());

        if (viewer.hasPermission(ticket.getCategory().getType().getPermission())) {
            if (ticket.getAssignedStaffUUID() == null) {
                inv.setItem(31, new ItemBuilder(Material.GREEN_CONCRETE)
                        .setName("§aKabul Et")
                        .addLore("Bu Ticketı Üstlen")
                        .setNBT("ticket_detail_tab", "accept")
                        .build());
            } else if (ticket.getAssignedStaffUUID().equals(viewer.getUniqueId())) {
                inv.setItem(31, new ItemBuilder(Material.ORANGE_CONCRETE)
                        .setName("§6Bu ticketla zaten ilgileniyorsun")
                        .addLore("ilgilenmek istemiyorsan tıkla")
                        .setNBT("ticket_detail_tab", "deny")
                        .build());
            } else {
                inv.setItem(31, new ItemBuilder(Material.GRAY_CONCRETE).
                        setName("§cBu Ticket Zaten Sahiplenilmiş").
                        addLore("çok geç").build());
            }
        } else {
            inv.setItem(31, new ItemBuilder(Material.RED_CONCRETE)
                    .setName("§Yetkin yok")
                    .addLore("burayı görememen lazım")
                    .addLore("Bir yetkiliye haber ver")
                    .build());
        }

        viewer.openInventory(inv);
    }

}
