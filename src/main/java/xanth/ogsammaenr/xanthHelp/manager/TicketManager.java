package xanth.ogsammaenr.xanthHelp.manager;

import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.model.Ticket;
import xanth.ogsammaenr.xanthHelp.model.TicketStatus;
import xanth.ogsammaenr.xanthHelp.storage.TicketDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TicketManager {
    private final XanthHelp plugin;
    private final TicketDAO ticketDAO;

    public TicketManager(XanthHelp plugin) {
        this.plugin = plugin;
        ticketDAO = new TicketDAO(plugin);
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

}
