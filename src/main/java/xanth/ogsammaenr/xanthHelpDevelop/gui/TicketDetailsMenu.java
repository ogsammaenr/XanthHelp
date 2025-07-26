package xanth.ogsammaenr.xanthHelpDevelop.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.manager.PingStaffManager;
import xanth.ogsammaenr.xanthHelpDevelop.model.*;
import xanth.ogsammaenr.xanthHelpDevelop.util.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketDetailsMenu {
    private final XanthHelp plugin;
    private final GuiConfig.TicketDetailMenuConfig config;
    private final PingStaffManager pingManager;

    public TicketDetailsMenu() {
        this.plugin = XanthHelp.getInstance();
        this.config = plugin.getGuiConfigManager().getTicketDetailMenuConfig();
        this.pingManager = plugin.getPingStaffManager();
    }

    public void open(@NotNull Player player, @NotNull Ticket ticket) {
        UUID playerId = player.getUniqueId();

        String title = config.getMenuTitle();
        int rows = config.getRows();

        CustomInventoryHolder holder = new CustomInventoryHolder(MenuType.TICKET_DETAILS_MENU);
        Inventory inventory = Bukkit.createInventory(holder, rows * 9, title + "§8 - §7" + ticket.getTicketId());
        holder.setInventory(inventory);
        holder.putData(GuiDataKey.TICKET_ID, ticket.getTicketId());

        GuiUtils.placeFillers(config.getFillerSettings(), inventory, rows);

        inventory.setItem(13, getItem(config.getIdButton(), ticket));
        inventory.setItem(20, getItem(config.getOwnerName(), ticket));
        inventory.setItem(21, getItem(config.getStatus(), ticket));
        inventory.setItem(22, getItem(config.getDescription(), ticket));
        inventory.setItem(23, getItem(config.getCategory(), ticket));
        inventory.setItem(24, getItem(config.getStaffName(), ticket));
        inventory.setItem(29, getItem(config.getCreationDate(), ticket));
        inventory.setItem(30, getItem(config.getAssignationDate(), ticket));
        inventory.setItem(32, getItem(config.getResolveDate(), ticket));
        inventory.setItem(33, getItem(config.getParticipants(), ticket));

        if (ticket.getCreatorId().equals(playerId)
            && (ticket.getStatus() != TicketStatus.RESOLVED || ticket.getStatus() != TicketStatus.CANCELED)) {
            ItemStack cancelButton = new ItemBuilder(config.getCancelTicket())
                    .setNBT("button", "cancelTicket")
                    .build();
            inventory.setItem(40, cancelButton);
            if (ticket.getStatus() == TicketStatus.OPEN) {
                if (pingManager.isPlayerWaiting(player.getUniqueId())) {
                    ItemStack pingStaff = new ItemBuilder(config.getPingStaff_enabled())
                            .setNBT("button", "pingTicket")
                            .build();
                    inventory.setItem(53, pingStaff);
                } else {
                    ItemStack pingStaff = config.getPingStaff_cooldown();
                    ItemMeta meta = pingStaff.getItemMeta();
                    if (meta.hasLore()) {
                        List<String> lore = new ArrayList<>();
                        for (String line : meta.getLore()) {
                            lore.add(line.replace("%cooldown%", pingManager.getCooldown(playerId)));
                        }
                        meta.setLore(lore);
                    }
                    if (meta.hasDisplayName()) {
                        String displayName = meta.getDisplayName();
                        displayName = displayName.replace("%cooldown%", pingManager.getCooldown(playerId));
                        meta.setDisplayName(displayName);
                    }
                    pingStaff.setItemMeta(meta);

                    inventory.setItem(53, pingStaff);
                }

            }
        } else if (player.hasPermission(ticket.getCategory().getType().getPermission())) {

        }

        player.openInventory(inventory);
    }

    private ItemStack getItem(ItemStack item, Ticket ticket) {
        if (!item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(GuiUtils.replacePlaceholders(meta.getDisplayName(), ticket));
        if (meta.hasLore()) {
            List<String> lore = new ArrayList<>();
            for (String line : meta.getLore()) {
                line = GuiUtils.replacePlaceholders(line, ticket);
                lore.add(line);
            }
            meta.setLore(lore);
        }

        item.setItemMeta(meta);
        return item;
    }

}
