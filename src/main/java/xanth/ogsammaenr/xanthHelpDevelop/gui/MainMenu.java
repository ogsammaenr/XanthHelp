package xanth.ogsammaenr.xanthHelpDevelop.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.model.GuiConfig;
import xanth.ogsammaenr.xanthHelpDevelop.model.MenuType;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategoryType;

import java.util.Map;

public class MainMenu {
    private XanthHelp plugin;
    private GuiConfig.MainMenuConfig mainMenuConfig;

    public MainMenu() {
        this.plugin = XanthHelp.getInstance();
        this.mainMenuConfig = plugin.getGuiConfigManager().getMainMenuConfig();
    }

    public void open(Player player) {
        String title = mainMenuConfig.getTitle();
        int rows = mainMenuConfig.getRows();
        Map<TicketCategoryType, Integer> buttons = mainMenuConfig.getCategoryTypeButtons();

        CustomInventoryHolder holder = new CustomInventoryHolder(MenuType.MAIN_MENU);
        Inventory inventory = Bukkit.createInventory(holder, rows * 9, title);
        holder.setInventory(inventory);

        GuiUtils.placeFillers(mainMenuConfig.getFillerSettings(), inventory, rows);

        for (TicketCategoryType type : buttons.keySet()) {
            inventory.setItem(buttons.get(type), type.toItemStack());
        }

        player.openInventory(inventory);
    }

}
