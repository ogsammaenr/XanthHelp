package xanth.ogsammaenr.xanthHelp.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.manager.CategoryManager;
import xanth.ogsammaenr.xanthHelp.manager.GuiConfigManager;
import xanth.ogsammaenr.xanthHelp.model.Category;
import xanth.ogsammaenr.xanthHelp.model.CategoryType;
import xanth.ogsammaenr.xanthHelp.model.GuiCategoryItem;

import java.util.List;

public class CategoryMenu {
    private final XanthHelp plugin;

    public CategoryMenu(XanthHelp plugin) {
        this.plugin = plugin;
    }

    /**
     * Belirli bir kategori tipi için GUI'yi oluşturur ve oyuncuya gösterir.
     *
     * @param player         GUI'yi görecek oyuncu
     * @param categoryTypeId GUI'de gösterilecek kategori tipi (örnek: "general", "rules" vs.)
     */
    public void open(Player player, String categoryTypeId) {
        CategoryManager categoryManager = plugin.getCategoryManager();
        GuiConfigManager guiConfigManager = plugin.getGuiConfigManager();

        CategoryType type = categoryManager.getCategoryType(categoryTypeId);
        if (type == null) {
            player.sendMessage("§cBu kategori türü bulunamadı.");
            return;
        }

        List<Category> categories = categoryManager.getCategoriesByType(categoryTypeId);
        if (categories.isEmpty()) {
            player.sendMessage("§cBu kategori tipine ait herhangi bir kategori bulunmadı.");
            return;
        }

        // Genel GUI ayarları
        int rows = guiConfigManager.getCategoryMenuRows(categoryTypeId);
        String title = guiConfigManager.getCategoryMenuTitle(categoryTypeId);
        Inventory gui = Bukkit.createInventory(null, rows * 9, title);

        // Filler item
        ItemStack filler = guiConfigManager.getCategoryFillerItem();
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, filler);
        }

        // Kategorileri yerleştir
        for (Category category : categories) {
            GuiCategoryItem item = guiConfigManager.getCategoryItems().get(category.getId());
            if (item == null) {
                plugin.getLogger().warning("GUI için tanımlı olmayan kategori: " + category.getId());
                continue;
            }

            gui.setItem(item.getSlot(), item.toItemStack());
        }

        player.openInventory(gui);
    }
}

