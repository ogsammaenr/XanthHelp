package xanth.ogsammaenr.xanthHelp.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.model.GuiCategoryTypeItem;
import xanth.ogsammaenr.xanthHelp.util.ItemBuilder;

import java.util.Map;

public class MainMenu {

    private final XanthHelp plugin;

    public MainMenu(XanthHelp plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        int rows = plugin.getGuiConfigManager().getMainMenuRows();
        String title = plugin.getGuiConfigManager().getMainMenuTitle().replace("&", "§");

        int playerTicketsSlot = 4 + rows * 9;
        Inventory gui = Bukkit.createInventory(null, rows * 9, title);

        // Arka plan eşyalarını al ve doldur
        ItemStack filler = plugin.getGuiConfigManager().getCategoryTypeFillerItem();
        if (filler != null) {
            for (int i = 0; i < gui.getSize(); i++) {
                gui.setItem(i, filler);
            }
        }

        // Kategori türlerini al ve GUI’ye yerleştir
        Map<String, GuiCategoryTypeItem> typeItems = plugin.getGuiConfigManager().getCategoryTypeItems();
        for (GuiCategoryTypeItem item : typeItems.values()) {
            gui.setItem(item.getSlot(), item.toItemStack());
        }
        gui.setItem(playerTicketsSlot, new ItemBuilder(Material.BOOK).setName("§fTaleplerim").build());

        player.openInventory(gui);
    }
}
