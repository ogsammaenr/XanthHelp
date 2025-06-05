package xanth.ogsammaenr.xanthHelp.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
    private final String ticketId;
    private final UUID creatorUUID;
    private final Category category;
    private TicketStatus status;
    private final String description;
    private final LocalDateTime createdAt;

    private UUID assignedStaffUUID;
    private LocalDateTime assignedAt;
    private LocalDateTime resolvedAt;

    public Ticket(String ticketId, UUID creatorUUID, Category category, String description, LocalDateTime createdAt) {
        this.ticketId = ticketId;
        this.creatorUUID = creatorUUID;
        this.category = category;
        this.description = description;
        this.status = TicketStatus.OPEN;
        this.createdAt = createdAt;
    }

    // Getters
    public String getTicketId() {
        return ticketId;
    }

    public UUID getCreatorUUID() {
        return creatorUUID;
    }

    public Category getCategory() {
        return category;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UUID getAssignedStaffUUID() {
        return assignedStaffUUID;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    // Setters
    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public void setAssignedStaffUUID(UUID assignedStaffUUID) {
        this.assignedStaffUUID = assignedStaffUUID;
    }

    public void assignStaff(UUID staffUUID) {
        this.assignedStaffUUID = staffUUID;
        this.assignedAt = LocalDateTime.now();
        this.status = TicketStatus.IN_PROGRESS;
    }

    public void markResolved() {
        this.resolvedAt = LocalDateTime.now();
        this.status = TicketStatus.RESOLVED;
    }

    public void markCanceled() {
        this.status = TicketStatus.CANCELED;
    }
}
