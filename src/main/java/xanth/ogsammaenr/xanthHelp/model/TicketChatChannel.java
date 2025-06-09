package xanth.ogsammaenr.xanthHelp.model;

import java.util.*;

public class TicketChatChannel {
    private final String ticketId;
    private final UUID owner;
    private final UUID assignedStaff;

    private final Set<UUID> activeParticipants = new HashSet<>();
    private final Map<UUID, Boolean> pendingRequests = new HashMap<>();

    public TicketChatChannel(String ticketId, UUID owner, UUID assignedStaff) {
        this.ticketId = ticketId;
        this.owner = owner;
        this.assignedStaff = assignedStaff;

        // Varsayılan olarak owner ve yetkili kanala erişebilir
        activeParticipants.add(owner);
        activeParticipants.add(assignedStaff);
    }

    public String getTicketId() {
        return ticketId;
    }

    public UUID getOwner() {
        return owner;
    }

    public UUID getAssignedStaff() {
        return assignedStaff;
    }

    public Set<UUID> getActiveParticipants() {
        return Collections.unmodifiableSet(activeParticipants);
    }

    public boolean isParticipant(UUID player) {
        return activeParticipants.contains(player);
    }

    public void joinChannel(UUID player) {
        activeParticipants.add(player);
        pendingRequests.remove(player);
    }

    public void requestAccess(UUID player) {
        pendingRequests.put(player, false);
    }

    public boolean hasRequested(UUID player) {
        return pendingRequests.containsKey(player);
    }

    public boolean isRequestPending(UUID player) {
        return !pendingRequests.getOrDefault(player, false);
    }

    public void approveRequest(UUID player) {
        pendingRequests.put(player, true);
        joinChannel(player);
    }

    public Map<UUID, Boolean> getPendingRequests() {
        return Collections.unmodifiableMap(pendingRequests);
    }

    public boolean isAccessApproved(UUID player) {
        return pendingRequests.getOrDefault(player, false);
    }


}
