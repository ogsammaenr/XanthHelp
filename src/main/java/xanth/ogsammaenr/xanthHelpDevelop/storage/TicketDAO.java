package xanth.ogsammaenr.xanthHelpDevelop.storage;

import xanth.ogsammaenr.xanthHelpDevelop.model.Ticket;

import java.util.List;
import java.util.Optional;

/**
 * TicketDAO defines persistence operations for Ticket objects.
 */
public interface TicketDAO {
    /**
     * Retrieve all tickets from storage.
     */
    List<Ticket> findAll();

    /**
     * Find a ticket by its ID.
     */
    Optional<Ticket> findById(String ticketId);

    /**
     * Insert a new ticket into storage.
     */
    void insert(Ticket ticket);

    /**
     * Update an existing ticket in storage.
     */
    void update(Ticket ticket);

    /**
     * Delete a ticket by its ID from storage.
     */
    void delete(String ticketId);
}
