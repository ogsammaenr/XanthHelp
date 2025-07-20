package xanth.ogsammaenr.xanthHelpDevelop.model;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class GuiConfig {

    public static class ConfirmationMenuConfig {
        private String title;
        private int rows;
        private FillerSettings fillerSettings;
        private ItemStack acceptButton;
        private ItemStack denyButton;

        public ConfirmationMenuConfig(String title, int rows, FillerSettings fillerSettings, ItemStack denyButton, ItemStack acceptButton) {
            this.title = title;
            this.rows = rows;
            this.fillerSettings = fillerSettings;
            this.denyButton = denyButton;
            this.acceptButton = acceptButton;
        }

        public FillerSettings getFillerSettings() {
            return fillerSettings;
        }

        public int getRows() {
            return rows;
        }

        public ItemStack getAcceptedButton() {
            return acceptButton;
        }

        public ItemStack getDenyButton() {
            return denyButton;
        }

        public String getTitle() {
            return title;
        }
    }

    public static class MainMenuConfig {
        private String title;
        private int rows;
        private FillerSettings fillerSettings;
        private Map<TicketCategoryType, Integer> categoryTypeButtons;

        public MainMenuConfig(String title, int rows, FillerSettings fillerSettings, Map<TicketCategoryType, Integer> categoryTypeButtons) {
            this.title = title;
            this.rows = rows;
            this.fillerSettings = fillerSettings;
            this.categoryTypeButtons = categoryTypeButtons;
        }

        public FillerSettings getFillerSettings() {
            return fillerSettings;
        }

        public String getTitle() {
            return title;
        }

        public int getRows() {
            return rows;
        }

        public Map<TicketCategoryType, Integer> getCategoryTypeButtons() {
            return categoryTypeButtons;
        }
    }

    public static class TicketCategoriesMenuConfig {
        private String title;
        private int rows;
        private FillerSettings fillerSettings;
        private Map<TicketCategoryType, Map<TicketCategory, Integer>> categoryButtons;

        public TicketCategoriesMenuConfig(String title, int rows, FillerSettings fillerSettings, Map<TicketCategoryType, Map<TicketCategory, Integer>> categoryButtons) {
            this.title = title;
            this.rows = rows;
            this.fillerSettings = fillerSettings;
            this.categoryButtons = categoryButtons;
        }

        public FillerSettings getFillerSettings() {
            return fillerSettings;
        }

        public String getMenuTitle() {
            return title;
        }

        public int getRows() {
            return rows;
        }

        public Map<TicketCategoryType, Map<TicketCategory, Integer>> getCategoryButtons() {
            return categoryButtons;
        }
    }

    public static class TicketDetailMenuConfig {
        private String title;
        private int rows;
        private FillerSettings fillerSettings;

        private String id, ownerName, status, category, description, creationDate, staffName, assignationDate,
                resolveDate, participants;

        private ItemStack claim, markAsResolved, joinParticipants, leaveParticipants, unclaim, cancelTicket,
                pingStaff_enabled, pingStaff_cooldown;

        public TicketDetailMenuConfig(String title, int rows, FillerSettings fillerSettings
                , String id, String ownerName, String status, String category
                , String description, String creationDate, String staffName, String assignationDate
                , String resolveDate, String participants, ItemStack claim, ItemStack markAsResolved
                , ItemStack joinParticipants, ItemStack leaveParticipants, ItemStack unclaim
                , ItemStack cancelTicket, ItemStack pingStaff_enabled, ItemStack pingStaff_cooldown) {

            this.title = title;
            this.rows = rows;
            this.fillerSettings = fillerSettings;
            this.id = id;
            this.ownerName = ownerName;
            this.status = status;
            this.category = category;
            this.description = description;
            this.creationDate = creationDate;
            this.staffName = staffName;
            this.assignationDate = assignationDate;
            this.resolveDate = resolveDate;
            this.participants = participants;
            this.claim = claim;
            this.markAsResolved = markAsResolved;
            this.joinParticipants = joinParticipants;
            this.leaveParticipants = leaveParticipants;
            this.unclaim = unclaim;
            this.cancelTicket = cancelTicket;
            this.pingStaff_enabled = pingStaff_enabled;
            this.pingStaff_cooldown = pingStaff_cooldown;
        }

        public FillerSettings getFillerSettings() {
            return fillerSettings;
        }

        public int getRows() {
            return rows;
        }

        public String getMenuTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getIdButtonMsg() {
            return id;
        }

        public ItemStack getCancelTicket() {
            return cancelTicket;
        }

        public ItemStack getClaim() {
            return claim;
        }

        public ItemStack getJoinParticipants() {
            return joinParticipants;
        }

        public ItemStack getLeaveParticipants() {
            return leaveParticipants;
        }

        public ItemStack getMarkAsResolved() {
            return markAsResolved;
        }

        public ItemStack getPingStaff_cooldown() {
            return pingStaff_cooldown;
        }

        public ItemStack getPingStaff_enabled() {
            return pingStaff_enabled;
        }

        public ItemStack getUnclaim() {
            return unclaim;
        }

        public String getAssignationDateMsg() {
            return assignationDate;
        }

        public String getCategoryMsg() {
            return category;
        }

        public String getCreationDateMsg() {
            return creationDate;
        }

        public String getOwnerNameMsg() {
            return ownerName;
        }

        public String getParticipantsMsg() {
            return participants;
        }

        public String getResolveDateMsg() {
            return resolveDate;
        }

        public String getStaffNameMsg() {
            return staffName;
        }

        public String getStatusMsg() {
            return status;
        }
    }

    public static class TicketsMenuConfig {
        private String title;
        private int rows;
        private FillerSettings fillerSettings;
        private int[] ticketButtons;

        private ItemStack next, previous, playerFilterButton, open, inProgress, cancelled, resolved, all;

        public TicketsMenuConfig(String title, int rows, FillerSettings fillerSettings
                , int[] ticketButtons, ItemStack next, ItemStack previous
                , ItemStack playerFilterButton, ItemStack open
                , ItemStack inProgress, ItemStack cancelled
                , ItemStack resolved, ItemStack all) {
            this.title = title;
            this.rows = rows;
            this.fillerSettings = fillerSettings;
            this.ticketButtons = ticketButtons;
            this.next = next;
            this.previous = previous;
            this.playerFilterButton = playerFilterButton;
            this.open = open;
            this.inProgress = inProgress;
            this.cancelled = cancelled;
            this.resolved = resolved;
            this.all = all;
        }

        public String getTitle() {
            return title;
        }

        public FillerSettings getFillerSettings() {
            return fillerSettings;
        }

        public int getRows() {
            return rows;
        }

        public int[] getTicketButtons() {
            return ticketButtons;
        }

        public ItemStack getAll() {
            return all;
        }

        public ItemStack getCancelled() {
            return cancelled;
        }

        public ItemStack getInProgress() {
            return inProgress;
        }

        public ItemStack getNext() {
            return next;
        }

        public ItemStack getOpen() {
            return open;
        }

        public ItemStack getPlayerFilterButton() {
            return playerFilterButton;
        }

        public ItemStack getPrevious() {
            return previous;
        }

        public ItemStack getResolved() {
            return resolved;
        }
    }

    public static class FillerSettings {
        private ItemStack topRow;
        private ItemStack bottomRow;
        private ItemStack general;

        public FillerSettings(ItemStack topRow, ItemStack bottomRow, ItemStack general) {
            this.topRow = topRow;
            this.bottomRow = bottomRow;
            this.general = general;
        }

        public ItemStack getBottomRow() {
            return bottomRow;
        }

        public ItemStack getGeneral() {
            return general;
        }

        public ItemStack getTopRow() {
            return topRow;
        }
    }

}
