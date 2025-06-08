package xanth.ogsammaenr.xanthHelp.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.gui.AdminSupportMenu;
import xanth.ogsammaenr.xanthHelp.gui.CategoryMenu;
import xanth.ogsammaenr.xanthHelp.gui.TicketDetailMenu;
import xanth.ogsammaenr.xanthHelp.manager.CategoryManager;
import xanth.ogsammaenr.xanthHelp.manager.GuiConfigManager;
import xanth.ogsammaenr.xanthHelp.manager.TicketManager;
import xanth.ogsammaenr.xanthHelp.model.Category;
import xanth.ogsammaenr.xanthHelp.model.TicketStatus;
import xanth.ogsammaenr.xanthHelp.util.Utils;

import java.sql.SQLException;

public class InventoryClickListener implements Listener {
    private final XanthHelp plugin;
    private final TicketManager ticketManager;
    private final GuiConfigManager guiConfigManager;
    private final CategoryManager categoryManager;

    public InventoryClickListener(XanthHelp plugin) {
        this.plugin = plugin;
        this.ticketManager = plugin.getTicketManager();
        this.guiConfigManager = plugin.getGuiConfigManager();
        this.categoryManager = plugin.getCategoryManager();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType().isAir()) {
            return; // Tıklanan slot boşsa işleme devam etme
        }

        // Envanter başlığını al
        String inventoryTitle = event.getView().getTitle();
        String mainMenuTitle = guiConfigManager.getMainMenuTitle().replace("&", "§");

        /// Ana Menü Kontrolü
        if (inventoryTitle.equals(mainMenuTitle)) {
            event.setCancelled(true);

            // Tıklanan itemda category_type NBT tag'ı var mı kontrol et
            String categoryTypeId = Utils.getStringTag(clicked, "category_type");
            if (categoryTypeId != null) {
                Player player = (Player) event.getWhoClicked();

                new CategoryMenu(plugin).open(player, categoryTypeId);
            }
        }

        /// Kategori Menüleri kontrolü
        String categoryTypeId = guiConfigManager.getCategoryTypeIdByMenuTitle(inventoryTitle);
        if (categoryTypeId != null) {
            event.setCancelled(true);

            String categoryId = Utils.getStringTag(clicked, "category_id");
            if (categoryId != null) {
                Player player = (Player) event.getWhoClicked();

                // Kategori objesini al
                Category category = categoryManager.getCategory(categoryId);
                if (category == null) {
                    player.sendMessage("§cGeçersiz kategori seçildi!");
                    player.closeInventory();
                    return;
                }

                // Oyuncunun açıklama girmesi için envanteri kapat ve mesaj gönder
                player.closeInventory();
                player.sendMessage("§aLütfen sorununuzu 1 cümle ile veya kısa şekilde açıklayın:");

                // Oyuncuyu chat moduna al, burada bir Set veya Map ile tutuyoruz örneğin:
                plugin.getChatInputListener().startWaitingForDescription(player.getUniqueId(), category);
            }
        }

        /// Admin Menü Kontrolü
        String adminMenuTitle = "§cYardım Talepleri";
        if (inventoryTitle.startsWith(adminMenuTitle)) {
            event.setCancelled(true);
            String[] parts = inventoryTitle.split(" ");
            int page = Integer.parseInt(parts[parts.length - 1]);
            String filter = parts[parts.length - 2];

            if (clicked != null && clicked.hasItemMeta()) {
                Player player = (Player) event.getWhoClicked();
                String tab = Utils.getStringTag(clicked, "admin_tab");
                if (tab != null && tab.equals("ALL")) {
                    new AdminSupportMenu(plugin, null, 0).open(player);
                } else if (tab != null && tab.equals("OPEN")) {
                    new AdminSupportMenu(plugin, TicketStatus.OPEN, 0).open(player);
                } else if (tab != null && tab.equals("IN_PROGRESS")) {
                    new AdminSupportMenu(plugin, TicketStatus.IN_PROGRESS, 0).open(player);
                } else if (tab != null && tab.equals("RESOLVED")) {
                    new AdminSupportMenu(plugin, TicketStatus.RESOLVED, 0).open(player);
                } else if (tab != null && tab.equals("CANCELED")) {
                    new AdminSupportMenu(plugin, TicketStatus.CANCELED, 0).open(player);
                } else if (tab != null && tab.equals("NEXT_PAGE")) {
                    new AdminSupportMenu(plugin, ((filter.equals("ALL")) ? null : TicketStatus.valueOf(filter)), page + 1).open(player);
                } else if (tab != null && tab.equals("PREVIOUS_PAGE")) {
                    new AdminSupportMenu(plugin, ((filter.equals("ALL")) ? null : TicketStatus.valueOf(filter)), page - 1).open(player);
                } else if (tab != null && tab.startsWith("TCK")) {
                    try {
                        new TicketDetailMenu(ticketManager.getTicketById(tab), player).open();
                    } catch (SQLException e) {
                        player.sendMessage("SQL tabanlı bir hata oluştu");
                        throw new RuntimeException(e);
                    }
                }
            }

        }

        /// Ticket Detayları Menüsü Kontrolü
        String ticketDetailMenuTitle = "Ticket Detayı";
        if (inventoryTitle.startsWith(ticketDetailMenuTitle)) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            String tab = Utils.getStringTag(clicked, "ticket_detail_tab");
            String[] parts = inventoryTitle.split(" ");
            String ticketId = parts[parts.length - 1];

            if (tab != null && tab.equals("previous_page")) {
                new AdminSupportMenu(plugin, null, 0).open(player);
            } else if (tab != null && tab.equals("accept")) {
                try {
                    ticketManager.assignTicket(ticketId, player.getUniqueId());
                    player.sendMessage("Bu Ticketla artık sen ilgileniyorsun");
                    player.closeInventory();
                } catch (SQLException e) {
                    player.sendMessage("§cSQL tabanlı bir hata oluştu");
                    throw new RuntimeException(e);
                }
            } else if (tab != null && tab.equals("deny")) {
                try {
                    ticketManager.unassignTicket(ticketId);
                    player.sendMessage("§eBu ticketla artık ilgilenmiyorsun");
                } catch (SQLException e) {
                    player.sendMessage("§cSQL tabanlı bir hata oluştu");
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
