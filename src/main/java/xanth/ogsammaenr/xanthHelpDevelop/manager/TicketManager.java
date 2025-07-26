package xanth.ogsammaenr.xanthHelpDevelop.manager;

import xanth.ogsammaenr.xanthHelpDevelop.model.Ticket;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketStatus;
import xanth.ogsammaenr.xanthHelpDevelop.storage.TicketDAO;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class TicketManager {
    private final TicketDAO ticketDAO;
    private final Map<String, Ticket> tickets = new ConcurrentHashMap<>();

    private int lastTicketNumber;

    public TicketManager(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    /**
     * Load all tickets from storage into memory cache.
     */
    public void loadTickets() {
        ticketDAO.findAll().forEach(ticket -> tickets.put(ticket.getTicketId(), ticket));
        lastTicketNumber = tickets.values().size() + 1;
    }

    /**
     * Returns a collection of all tickets in memory.
     */
    public Collection<Ticket> getAllTickets() {
        return tickets.values();
    }

    /**
     * Find a ticket by its ID.
     */
    public Optional<Ticket> getTicket(String ticketId) {
        return Optional.ofNullable(tickets.get(ticketId));
    }

    /**
     * Create a new ticket: persist and cache.
     */
    public Ticket createTicket(Ticket ticket) {
        // Persist first to ensure storage
        ticketDAO.insert(ticket);
        tickets.put(ticket.getTicketId(), ticket);
        return ticket;
    }

    /**
     * Update an existing ticket: persist and update cache.
     */
    public void updateTicket(Ticket ticket) {
        ticketDAO.update(ticket);
        tickets.put(ticket.getTicketId(), ticket);
    }

    /**
     * Delete a ticket: remove from storage and cache.
     */
    public void deleteTicket(String ticketId) {
        ticketDAO.delete(ticketId);
        tickets.remove(ticketId);
    }

    /**
     * Add a participant to a ticket and persist change.
     */
    public void addParticipant(String ticketId, UUID participant) {
        getTicket(ticketId).ifPresent(ticket -> {
            ticket.addParticipant(participant);
            updateTicket(ticket);
        });
    }

    /**
     * Add an active participant and persist if needed.
     */
    public void addActiveParticipant(String ticketId, UUID participant) {
        getTicket(ticketId).ifPresent(ticket -> {
            ticket.addActiveParticipant(participant);
            updateTicket(ticket);
        });
    }

    /**
     * Remove an active participant.
     */
    public void removeActiveParticipant(String ticketId, UUID participant) {
        getTicket(ticketId).ifPresent(ticket -> {
            ticket.removeActiveParticipant(participant);
            updateTicket(ticket);
        });
    }

    public int getLastTicketNumber() {
        return lastTicketNumber++;
    }

    /**
     * Retrieves tickets based on optional status and/or owner UUID filters.
     *
     * @param status  (nullable) TicketStatus to filter by
     * @param ownerId (nullable) Owner UUID to filter by
     * @return a collection of tickets that match the filters
     */
    public Collection<Ticket> getTicketsFiltered(TicketStatus status, UUID ownerId) {
        return tickets.values().stream()
                .filter(ticket -> status == null || ticket.getStatus() == status)
                .filter(ticket -> ownerId == null ||
                                  (ticket.getCreatorId() != null && ticket.getCreatorId().equals(ownerId)))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves tickets where the specified player is participating (as in participants list).
     * Duplicate of getTicketsForParticipant for clarity.
     *
     * @param participantId the UUID of the participant
     * @return a collection of tickets the player is participating in, or an empty list if none
     */
    public Collection<Ticket> getTicketsByParticipant(UUID participantId) {
        return tickets.values().stream()
                .filter(ticket -> ticket.getParticipants() != null && ticket.getParticipants().contains(participantId))
                .collect(Collectors.toList());
    }
}
