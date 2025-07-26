package xanth.ogsammaenr.xanthHelpDevelop.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xanth.ogsammaenr.xanthHelpDevelop.model.GuiConfig;
import xanth.ogsammaenr.xanthHelpDevelop.model.Ticket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuiUtils {

    protected static void placeFillers(GuiConfig.FillerSettings fillerSettings, Inventory inventory, int rows) {
        ItemStack topRow = fillerSettings.getTopRow();
        ItemStack bottomRow = fillerSettings.getBottomRow();
        ItemStack general = fillerSettings.getGeneral();

        for (int i = 0; i < rows * 9; i++) {
            if (topRow != null && i < 9) {
                inventory.setItem(i, topRow);
            } else if (bottomRow != null && i >= (rows - 1) * 9) {
                inventory.setItem(i, bottomRow);
            } else if (general != null) {
                inventory.setItem(i, general);
            }
        }

    }

    protected static String replacePlaceholders(String input, Ticket ticket) {

        return input
                .replace("%ticket-id%", java.lang.String.valueOf(ticket.getTicketId()))
                .replace("%owner-name%", ticket.getOwnerName())
                .replace("%category%", ticket.getCategory().getDisplayName())
                .replace("%status%", ticket.getStatus().getDisplayName())
                .replace("%staff-name%", ticket.getStaffName())
                .replace("%description%", ticket.getDescription())
                .replace("%creation-date%", formatDate(ticket.getCreationDate()))
                .replace("%assignment-date%", ticket.getAssignationDate() != null ? formatDate(ticket.getAssignationDate()) : "N/A")
                .replace("%resolve-date%", ticket.getResolveDate() != null ? formatDate(ticket.getResolveDate()) : "N/A")
                .replace("%participants%", getParticipantsNameList(ticket));
    }

    protected static String getParticipantsNameList(Ticket ticket) {
        List<String> names = new ArrayList<>();

        for (UUID uuid : ticket.getParticipants()) {
            names.add(Bukkit.getOfflinePlayer(uuid).getName());
        }
        return String.join("\n", names);
    }

    protected static String formatDate(LocalDateTime dateTime) {
        return dateTime != null
                ? dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                : "N/A";
    }
}
