---
title: Untitled doc (2)
---
<SwmSnippet path="/src/main/java/xanth/ogsammaenr/xanthHelpDevelop/model/Ticket.java" line="1">

---

&nbsp;

```java
package xanth.ogsammaenr.xanthHelpDevelop.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public String getTicketId() {
        return ticketId;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public TicketCategory getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public LocalDateTime getAssignationDate() {
        return assignationDate;
    }

    public LocalDateTime getResolveDate() {
        return resolveDate;
    }

    public List<UUID> getParticipants() {
        return participants;
    }

    public List<UUID> getActiveParticipants() {
        return activeParticipants;
    }

    /* ==== SETTERS & HELPERS ==== */

    public void setParticipants(List<UUID> participants) {
        this.participants = participants;
    }

    public void setActiveParticipants(List<UUID> activeParticipants) {
        this.activeParticipants = activeParticipants;
    }

    public void addParticipant(UUID uuid) {
        if (this.participants == null) {
            this.participants = new ArrayList<>();
        }
        if (!this.participants.contains(uuid)) {
            this.participants.add(uuid);
        }
    }

    public void addActiveParticipant(UUID uuid) {
        if (this.activeParticipants == null) {
            this.activeParticipants = new ArrayList<>();
        }
        if (!this.activeParticipants.contains(uuid)) {
            this.activeParticipants.add(uuid);
        }
    }

    public void removeParticipant(UUID uuid) {
        if (this.participants != null) {
            this.participants.remove(uuid);
        }
    }

    public void removeActiveParticipant(UUID uuid) {
        if (this.activeParticipants != null) {
            this.activeParticipants.remove(uuid);
        }
    }

    // === BUILDER ===

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

        private List<UUID> participants;
        private List<UUID> activeParticipants;

        public Builder(String ticketId, UUID creatorId, TicketStatus status, TicketCategory category,
                       String description, LocalDateTime creationDate) {
            this.ticketId = ticketId;
            this.creatorId = creatorId;
            this.status = status;
            this.category = category;
            this.description = description;
            this.creationDate = creationDate;
        }

        public Builder setStaffId(UUID staffId) {
            this.staffId = staffId;
            return this;
        }

        public Builder setAssignationDate(LocalDateTime assignationDate) {
            this.assignationDate = assignationDate;
            return this;
        }

        public Builder setResolveDate(LocalDateTime resolveDate) {
            this.resolveDate = resolveDate;
            return this;
        }

        public Builder setParticipants(List<UUID> participants) {
            this.participants = participants;
            return this;
        }

        public Builder setActiveParticipants(List<UUID> activeParticipants) {
            this.activeParticipants = activeParticipants;
            return this;
        }

        public Ticket build() {
            Ticket ticket = new Ticket();

            ticket.ticketId = this.ticketId;
            ticket.creatorId = this.creatorId;
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

```

---

</SwmSnippet>

<SwmMeta version="3.0.0" repo-id="Z2l0aHViJTNBJTNBWGFudGhIZWxwJTNBJTNBb2dzYW1tYWVucg==" repo-name="XanthHelp"><sup>Powered by [Swimm](https://app.swimm.io/)</sup></SwmMeta>
