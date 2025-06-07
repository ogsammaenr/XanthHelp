package xanth.ogsammaenr.xanthHelp.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.gui.CategoryMenu;
import xanth.ogsammaenr.xanthHelp.model.Category;
import xanth.ogsammaenr.xanthHelp.util.Utils;

public class InventoryClickListener implements Listener {
    XanthHelp plugin;

    public InventoryClickListener(XanthHelp plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType().isAir()) {
            return; // Tıklanan slot boşsa işleme devam etme
        }

        // Envanter başlığını al
        String inventoryTitle = event.getView().getTitle();
        String mainMenuTitle = plugin.getGuiConfigManager().getMainMenuTitle().replace("&", "§");

        /// Ana Menü Kontrolü
        if (inventoryTitle.equals(mainMenuTitle)) {
            event.setCancelled(true);

            // Tıklanan itemda category_type NBT tag'ı var mı kontrol et
            String categoryTypeId = Utils.getStringTag(clicked, "category_type");
            if (categoryTypeId != null) {
                // Menüde tıklamayı iptal et
                Player player = (Player) event.getWhoClicked();

                new CategoryMenu(plugin).open(player, categoryTypeId);
            }
        }

        /// Kategori Menüleri kontrolü
        String categoryTypeId = plugin.getGuiConfigManager().getCategoryTypeIdByMenuTitle(inventoryTitle);
        if (categoryTypeId != null) {
            event.setCancelled(true);

            String categoryId = Utils.getStringTag(clicked, "category_id");
            if (categoryId != null) {
                Player player = (Player) event.getWhoClicked();

                // Kategori objesini al
                Category category = plugin.getCategoryManager().getCategory(categoryId);
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
    }
}
