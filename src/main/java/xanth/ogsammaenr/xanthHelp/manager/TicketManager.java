package xanth.ogsammaenr.xanthHelp.manager;

import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.model.Ticket;
import xanth.ogsammaenr.xanthHelp.model.TicketStatus;
import xanth.ogsammaenr.xanthHelp.storage.TicketDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TicketManager {
    private final XanthHelp plugin;
    private final TicketDAO ticketDAO;

    public TicketManager(XanthHelp plugin) {
        this.plugin = plugin;
        this.ticketDAO = new TicketDAO(plugin);
    }

    public void createTicket(Ticket ticket) throws SQLException {
        ticketDAO.createTicket(ticket);
    }

    public Ticket getTicketById(String ticketId) throws SQLException {
        return ticketDAO.getTicketById(ticketId);
    }

    public List<Ticket> getTicketsByCreator(UUID creatorUUID) throws SQLException {
        return ticketDAO.getTicketsByCreatorUUID(creatorUUID.toString());
    }

    public Ticket getActiveTicketByCreator(UUID creatorUUID) throws SQLException {
        List<Ticket> tickets = ticketDAO.getTicketsByCreatorUUID(creatorUUID.toString());
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == TicketStatus.IN_PROGRESS) {
                return ticket;
            }
        }
        return null;
    }

    public Ticket getActiveTicketByStaff(UUID staffUUID) throws SQLException {
        List<Ticket> tickets = ticketDAO.getAllTickets();
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == TicketStatus.IN_PROGRESS
                    && staffUUID.equals(ticket.getAssignedStaffUUID())) {
                return ticket;
            }
        }
        return null;
    }

    public Optional<Ticket> getActiveTicketOf(UUID uuid) {
        return ticketDAO.getActiveTicketByUser(uuid);
    }

    public void assignTicket(String ticketId, UUID staffUUID) throws SQLException {
        Ticket ticket = ticketDAO.getTicketById(ticketId);
        if (ticket != null && ticket.getStatus() == TicketStatus.OPEN) {
            ticket.setAssignedStaffUUID(staffUUID);
            ticket.setAssignedAt(LocalDateTime.now());
            ticket.setStatus(TicketStatus.IN_PROGRESS);
            ticketDAO.updateTicketStatus(ticketId, TicketStatus.IN_PROGRESS);
            ticketDAO.updateAssignedStaff(ticketId, staffUUID, ticket.getAssignedAt());
        }
    }

    public void assignTicketSafely(String ticketId, UUID staffUUID) throws SQLException {
        if (hasActiveTicket(staffUUID)) {
            throw new IllegalStateException("Yetkili zaten bir ticket ile ilgileniyor.");
        }

        Ticket ticket = getTicketById(ticketId);
        if (ticket.getStatus() != TicketStatus.OPEN) {
            throw new IllegalStateException("Ticket zaten işleniyor.");
        }

        assignTicket(ticketId, staffUUID);
    }

    public void unassignTicket(String ticketId) throws SQLException {
        Ticket ticket = getTicketById(ticketId);
        if (ticket == null || ticket.getStatus() != TicketStatus.IN_PROGRESS) {
            return;
        }
        ticket.setAssignedStaffUUID(null);
        ticket.setAssignedAt(null);
        ticket.setStatus(TicketStatus.OPEN);

        try {
            ticketDAO.updateAssignedStaff(ticketId, null, null);
            ticketDAO.updateTicketStatus(ticketId, TicketStatus.OPEN);

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void cancelTicket(String ticketId) throws SQLException {
        Ticket ticket = getTicketById(ticketId);
        if (ticket != null && ticket.getStatus() != TicketStatus.CANCELED) {
            ticket.setStatus(TicketStatus.CANCELED);
            ticketDAO.updateTicketStatus(ticketId, TicketStatus.CANCELED);
        }
    }

    public void resolveTicket(String ticketId) throws SQLException {
        Ticket ticket = ticketDAO.getTicketById(ticketId);
        if (ticket != null && (ticket.getStatus() == TicketStatus.IN_PROGRESS || ticket.getStatus() == TicketStatus.OPEN)) {
            ticket.setStatus(TicketStatus.RESOLVED);
            ticket.setResolvedAt(LocalDateTime.now());
            ticketDAO.updateTicketStatus(ticketId, TicketStatus.RESOLVED);
            ticketDAO.updateResolvedAt(ticketId, ticket.getResolvedAt());
        }
    }

    public void deleteTicket(String ticketId) throws SQLException {
        ticketDAO.deleteTicket(ticketId);
    }

    public int getNextTicketNumber() throws SQLException {
        return ticketDAO.getNextTicketNumberUsingCount();
    }

    public List<Ticket> getAllTickets() throws SQLException {
        return ticketDAO.getAllTickets();
    }

    public List<Ticket> getTicketsByStatus(TicketStatus status) throws SQLException {
        return ticketDAO.getTicketsByStatus(status);
    }

    public boolean hasActiveTicket(UUID creatorUUID) throws SQLException {
        return getActiveTicketByCreator(creatorUUID) != null;
    }

    public boolean staffHasActiveTicket(UUID staffUUID) throws SQLException {
        return getActiveTicketByStaff(staffUUID) != null;
    }

}
