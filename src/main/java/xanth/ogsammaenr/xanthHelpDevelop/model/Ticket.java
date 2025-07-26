package xanth.ogsammaenr.xanthHelpDevelop.model;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The type Ticket.
 */
public class Ticket {
    private String ticketId;

    private UUID creatorId;
    private TicketStatus status;
    private TicketCategory category;
    private String description;
    private LocalDateTime creationDate;

    private UUID staffId;
    private LocalDateTime assignationDate;
    private LocalDateTime resolveDate;

    private List<UUID> participants;
    private List<UUID> activeParticipants;

    private Ticket() {
        // private constructor to force use of Builder
    }



    /* ==== GETTERS ==== */

    /**
     * Gets ticket ıd.
     *
     * @return the ticket ıd
     */
    public String getTicketId() {
        return ticketId;
    }

    /**
     * Gets creator ıd.
     *
     * @return the creator ıd
     */
    public UUID getCreatorId() {
        return creatorId;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public TicketStatus getStatus() {
        return status;
    }

    /**
     * Gets category.
     *
     * @return the category
     */
    public TicketCategory getCategory() {
        return category;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets creation date.
     *
     * @return the creation date
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Gets staff ıd.
     *
     * @return the staff ıd
     */
    public UUID getStaffId() {
        return staffId;
    }

    /**
     * Gets assignation date.
     *
     * @return the assignation date
     */
    public LocalDateTime getAssignationDate() {
        return assignationDate;
    }

    /**
     * Gets resolve date.
     *
     * @return the resolve date
     */
    public LocalDateTime getResolveDate() {
        return resolveDate;
    }

    /**
     * Gets participants.
     *
     * @return the participants
     */
    public List<UUID> getParticipants() {
        return participants;
    }

    /**
     * Gets active participants.
     *
     * @return the active participants
     */
    public List<UUID> getActiveParticipants() {
        return activeParticipants;
    }

    /* ==== SETTERS & HELPERS ==== */

    /**
     * Sets participants.
     *
     * @param participants the participants
     */
    public void setParticipants(List<UUID> participants) {
        this.participants = participants;
    }

    /**
     * Sets active participants.
     *
     * @param activeParticipants the active participants
     */
    public void setActiveParticipants(List<UUID> activeParticipants) {
        this.activeParticipants = activeParticipants;
    }

    /**
     * Add participant.
     *
     * @param uuid the uuid
     */
    public void addParticipant(UUID uuid) {
        if (this.participants == null) {
            this.participants = new ArrayList<>();
        }
        if (!this.participants.contains(uuid)) {
            this.participants.add(uuid);
        }
    }

    /**
     * Add active participant.
     *
     * @param uuid the uuid
     */
    public void addActiveParticipant(UUID uuid) {
        if (this.activeParticipants == null) {
            this.activeParticipants = new ArrayList<>();
        }
        if (!this.activeParticipants.contains(uuid)) {
            this.activeParticipants.add(uuid);
        }
    }

    /**
     * Remove participant.
     *
     * @param uuid the uuid
     */
    public void removeParticipant(UUID uuid) {
        if (this.participants != null) {
            this.participants.remove(uuid);
        }
    }

    /**
     * Remove active participant.
     *
     * @param uuid the uuid
     */
    public void removeActiveParticipant(UUID uuid) {
        if (this.activeParticipants != null) {
            this.activeParticipants.remove(uuid);
        }
    }

    public String getOwnerName() {
        OfflinePlayer owner = creatorId != null ? Bukkit.getOfflinePlayer(creatorId) : null;
        return owner.getName();
    }

    public String getStaffName() {
        if (staffId == null) return "N/A";
        OfflinePlayer owner = Bukkit.getOfflinePlayer(staffId);
        return owner.getName();
    }

    // === BUILDER ===

    /**
     * The type Builder.
     */
    public static class Builder {
        private final String ticketId;

        private final UUID creatorId;
        private final TicketStatus status;
        private final TicketCategory category;
        private final String description;
        private final LocalDateTime creationDate;

        private UUID staffId;
        private LocalDateTime assignationDate;
        private LocalDateTime resolveDate;

        private List<UUID> participants = new ArrayList<>();
        private List<UUID> activeParticipants = new ArrayList<>();

        /**
         * Instantiates a new Builder.
         *
         * @param ticketId     the ticket ıd
         * @param creatorId    the creator ıd
         * @param status       the status
         * @param category     the category
         * @param description  the description
         * @param creationDate the creation date
         */
        public Builder(String ticketId, UUID creatorId, TicketStatus status, TicketCategory category,
                       String description, LocalDateTime creationDate) {
            this.ticketId = ticketId;
            this.creatorId = creatorId;
            this.status = status;
            this.category = category;
            this.description = description;
            this.creationDate = creationDate;
            this.participants.add(creatorId);
        }

        /**
         * Sets staff ıd.
         *
         * @param staffId the staff ıd
         * @return the staff ıd
         */
        public Builder setStaffId(UUID staffId) {
            this.staffId = staffId;
            return this;
        }

        /**
         * Sets assignation date.
         *
         * @param assignationDate the assignation date
         * @return the assignation date
         */
        public Builder setAssignationDate(LocalDateTime assignationDate) {
            this.assignationDate = assignationDate;
            return this;
        }

        /**
         * Sets resolve date.
         *
         * @param resolveDate the resolve date
         * @return the resolve date
         */
        public Builder setResolveDate(LocalDateTime resolveDate) {
            this.resolveDate = resolveDate;
            return this;
        }

        /**
         * Sets participants.
         *
         * @param participants the participants
         * @return the participants
         */
        public Builder setParticipants(List<UUID> participants) {
            this.participants = participants;
            return this;
        }

        /**
         * Sets active participants.
         *
         * @param activeParticipants the active participants
         * @return the active participants
         */
        public Builder setActiveParticipants(List<UUID> activeParticipants) {
            this.activeParticipants = activeParticipants;
            return this;
        }

        /**
         * Build ticket.
         *
         * @return the ticket
         */
        public Ticket build() {
            Ticket ticket = new Ticket();

            ticket.ticketId = this.ticketId;
            if (ticketId == null) System.out.println(ticketId + " yok");
            ticket.creatorId = this.creatorId;
            if (creatorId == null) System.out.println(creatorId + " yok");
            ticket.status = this.status;
            ticket.category = this.category;
            ticket.description = this.description;
            ticket.creationDate = this.creationDate;
            ticket.staffId = this.staffId;
            ticket.assignationDate = this.assignationDate;
            ticket.resolveDate = this.resolveDate;
            ticket.participants = this.participants;
            ticket.activeParticipants = this.activeParticipants;

            return ticket;
        }
    }
}
