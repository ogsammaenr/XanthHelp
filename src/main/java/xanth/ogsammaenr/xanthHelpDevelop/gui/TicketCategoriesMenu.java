package xanth.ogsammaenr.xanthHelpDevelop.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.model.GuiConfig;
import xanth.ogsammaenr.xanthHelpDevelop.model.MenuType;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategory;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategoryType;

import java.util.Map;

public class TicketCategoriesMenu {
    private final XanthHelp plugin;
    private final GuiConfig.TicketCategoriesMenuConfig ticketCategoriesMenuConfig;

    public TicketCategoriesMenu() {
        this.plugin = XanthHelp.getInstance();
        this.ticketCategoriesMenuConfig = plugin.getGuiConfigManager().getTicketCategoriesMenuConfig();
    }

    public void open(Player player, TicketCategoryType type) {
        String title = ticketCategoriesMenuConfig.getMenuTitle();
        int rows = ticketCategoriesMenuConfig.getRows();
        Map<TicketCategoryType, Map<TicketCategory, Integer>> categoryButtons =
                ticketCategoriesMenuConfig.getCategoryButtons();

        CustomInventoryHolder holder = new CustomInventoryHolder(MenuType.TICKET_CATEGORIES_MENU);
        Inventory inventory = Bukkit.createInventory(holder, rows * 9, title);
        holder.setInventory(inventory);


        GuiUtils.placeFillers(ticketCategoriesMenuConfig.getFillerSettings(), inventory, rows);

        Map<TicketCategory, Integer> categories = categoryButtons.get(type);
        for (TicketCategory category : categories.keySet()) {
            inventory.setItem(categories.get(category), category.toItemStack());
        }

        player.openInventory(inventory);
    }

}
