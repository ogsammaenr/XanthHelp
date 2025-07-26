package xanth.ogsammaenr.xanthHelpDevelop.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.model.GuiConfig;
import xanth.ogsammaenr.xanthHelpDevelop.model.GuiDataKey;
import xanth.ogsammaenr.xanthHelpDevelop.model.MenuType;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategory;
import xanth.ogsammaenr.xanthHelpDevelop.util.ItemBuilder;

public class ConfirmationMenu {
    private final XanthHelp plugin;
    private final GuiConfig.ConfirmationMenuConfig config;

    private String ticketDescription;
    private TicketCategory ticketCategory;

    /**
     * For Ticket Create Confirmation
     *
     * @param ticketDescription
     * @param ticketCategory
     */
    public ConfirmationMenu(String ticketDescription, TicketCategory ticketCategory) {
        this.ticketDescription = ticketDescription;
        this.ticketCategory = ticketCategory;
        this.plugin = XanthHelp.getInstance();
        this.config = plugin.getGuiConfigManager().getConfirmationMenuConfig();
    }

    public ConfirmationMenu() {
        this.plugin = XanthHelp.getInstance();
        this.config = plugin.getGuiConfigManager().getConfirmationMenuConfig();
    }

    public void open(Player player, String confirmAction) {
        String title = config.getTitle();
        int rows = config.getRows();

        CustomInventoryHolder holder = new CustomInventoryHolder(MenuType.CONFIRMATION_MENU);
        Inventory inventory = Bukkit.createInventory(holder, rows * 9, title);
        holder.setInventory(inventory);
        holder.putData(GuiDataKey.CONFIRMATION_ACTION, confirmAction);
        if (confirmAction.equals("Create Ticket")) {
            holder.putData(GuiDataKey.TICKET_DESCRIPTION, ticketDescription);
            holder.putData(GuiDataKey.CATEGORY, ticketCategory);
        }

        GuiUtils.placeFillers(config.getFillerSettings(), inventory, rows);

        inventory.setItem(config.getAcceptSlot(), new ItemBuilder(config.getAcceptButton()
                .clone())
                .setNBT("button", "accept")
                .build());
        inventory.setItem(config.getDenySlot(), new ItemBuilder(config.getDenyButton()
                .clone())
                .setNBT("button", "deny")
                .build());

        player.openInventory(inventory);
    }


}
