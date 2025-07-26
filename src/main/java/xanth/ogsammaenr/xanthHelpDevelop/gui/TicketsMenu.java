package xanth.ogsammaenr.xanthHelpDevelop.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.manager.TicketManager;
import xanth.ogsammaenr.xanthHelpDevelop.model.*;
import xanth.ogsammaenr.xanthHelpDevelop.util.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketsMenu {
    private final XanthHelp plugin;
    private final GuiConfig.TicketsMenuConfig ticketsMenuConfig;
    private final TicketManager ticketManager;

    public TicketsMenu() {
        this.plugin = XanthHelp.getInstance();
        this.ticketManager = this.plugin.getTicketManager();
        this.ticketsMenuConfig = plugin.getGuiConfigManager().getTicketsMenuConfig();
    }

    public void open(@NotNull Player player, int page, @Nullable UUID filterId, @Nullable TicketStatus status) {
        String title = ticketsMenuConfig.getTitle();
        int rows = ticketsMenuConfig.getRows();

        CustomInventoryHolder holder = new CustomInventoryHolder(MenuType.TICKETS_MENU);
        Inventory inventory = Bukkit.createInventory(holder, rows * 9, title + "§8 - §7" + (page + 1));
        holder.setInventory(inventory);
        holder.putData(GuiDataKey.PAGE_NUMBER, page);
        holder.putData(GuiDataKey.STATUS, status);
        holder.putData(GuiDataKey.FILTER_UUID, filterId);

        GuiUtils.placeFillers(ticketsMenuConfig.getFillerSettings(), inventory, rows);

        List<Ticket> tickets = ticketManager.getTicketsFiltered(status, filterId)
                .stream()
                .toList();

        int[] ticketSlots = ticketsMenuConfig.getTicketButtons();
        int i = page * ticketSlots.length;

        for (int j = 0; j < ticketSlots.length; j++) {
            if (i + j >= tickets.size()) break;
            inventory.setItem(ticketSlots[j], ticketToItemStack(tickets.get(i + j)));
        }
        int totalPages = (int) Math.ceil((double) tickets.size() / ticketSlots.length);

        if (page < totalPages - 1) {
            inventory.setItem(ticketsMenuConfig.getNextSlot(), new ItemBuilder(ticketsMenuConfig
                    .getNext().clone())
                    .setNBT("button", "next")
                    .build());
        }
        if (page > 0) {
            inventory.setItem(ticketsMenuConfig.getPreviousSlot(), new ItemBuilder(ticketsMenuConfig
                    .getPrevious().clone())
                    .setNBT("button", "previous")
                    .build());
        }
        inventory.setItem(ticketsMenuConfig.getPlayerFilterButtonSlot(), new ItemBuilder(ticketsMenuConfig
                .getPlayerFilterButton().clone())
                .setNBT("button", "player-filter")
                .build());
        inventory.setItem(ticketsMenuConfig.getOpenSlot(), new ItemBuilder(ticketsMenuConfig
                .getOpen().clone())
                .setNBT("button", "OPEN")
                .addGlowIf(status == TicketStatus.OPEN)
                .build());
        inventory.setItem(ticketsMenuConfig.getInProgressSlot(), new ItemBuilder(ticketsMenuConfig
                .getInProgress().clone())
                .setNBT("button", "IN_PROGRESS")
                .addGlowIf(status == TicketStatus.IN_PROGRESS)
                .build());
        inventory.setItem(ticketsMenuConfig.getCancelledSlot(), new ItemBuilder(ticketsMenuConfig
                .getCancelled().clone())
                .setNBT("button", "CANCELLED")
                .addGlowIf(status == TicketStatus.CANCELED)
                .build());
        inventory.setItem(ticketsMenuConfig.getResolvedSlot(), new ItemBuilder(ticketsMenuConfig
                .getResolved().clone())
                .setNBT("button", "RESOLVED")
                .addGlowIf(status == TicketStatus.RESOLVED)
                .build());
        inventory.setItem(ticketsMenuConfig.getAllSlot(), new ItemBuilder(ticketsMenuConfig
                .getAll().clone())
                .setNBT("button", "ALL")
                .addGlowIf(status == null)
                .build());

        player.openInventory(inventory);
    }

    private ItemStack ticketToItemStack(Ticket ticket) {
        ItemStack format = ticketsMenuConfig.getTicketFormat().clone();

        List<String> formattedLore = new ArrayList<>();
        if (format.hasItemMeta() && format.getItemMeta().hasLore()) {
            for (String line : format.getItemMeta().getLore()) {
                formattedLore.add(GuiUtils.replacePlaceholders(line, ticket));
            }
        }

        String displayName = (format.hasItemMeta() && format.getItemMeta().hasDisplayName())
                ? GuiUtils.replacePlaceholders(format.getItemMeta().getDisplayName(), ticket)
                : "";

        return new ItemBuilder(format.getType())
                .setName(displayName)
                .setLore(formattedLore)
                .setNBT("ticketId", ticket.getTicketId())
                .build();
    }
    
}
