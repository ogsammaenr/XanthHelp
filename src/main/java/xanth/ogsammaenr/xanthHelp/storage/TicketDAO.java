package xanth.ogsammaenr.xanthHelp.storage;

import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.manager.CategoryManager;
import xanth.ogsammaenr.xanthHelp.model.Category;
import xanth.ogsammaenr.xanthHelp.model.Ticket;
import xanth.ogsammaenr.xanthHelp.model.TicketStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketDAO {
    private final DatabaseConnector connector;
    private CategoryManager categoryManager;

    public TicketDAO(XanthHelp plugin) {
        this.connector = plugin.getDatabaseConnector();
        this.categoryManager = plugin.getCategoryManager();
    }

    // Ticket oluşturma
    public void createTicket(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO tickets (ticket_id, creator_uuid, category_id, status, description, created_at, assigned_staff_uuid, assigned_at, resolved_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, ticket.getTicketId());
            stmt.setString(2, ticket.getCreatorUUID().toString());
            stmt.setString(3, ticket.getCategory().getId());
            stmt.setString(4, ticket.getStatus().name());
            stmt.setString(5, ticket.getDescription());
            stmt.setString(6, ticket.getCreatedAt().toString());
            stmt.setString(7, ticket.getAssignedStaffUUID() != null ? ticket.getAssignedStaffUUID().toString() : null);
            stmt.setString(8, ticket.getAssignedAt() != null ? ticket.getAssignedAt().toString() : null);
            stmt.setString(9, ticket.getResolvedAt() != null ? ticket.getResolvedAt().toString() : null);
            stmt.executeUpdate();
        }
    }

    // Ticket ID ile getirme
    public Ticket getTicketById(String ticketId) throws SQLException {
        String sql = "SELECT * FROM tickets WHERE ticket_id = ?";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, ticketId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Ticket ticket = mapResultSetToTicket(rs);

                // --- PARTICIPANTS LİSTESİNİ YÜKLE ---
                ticket.setParticipants(getParticipantsByTicketId(ticket.getTicketId()));

                return ticket;
            }
        }
        return null;
    }

    // Belirli kullanıcıya ait ticketları listeleme
    public List<Ticket> getTicketsByCreatorUUID(String creatorUUID) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE creator_uuid = ?";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, creatorUUID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        }
        return tickets;
    }

    // Ticket durumu güncelleme
    public void updateTicketStatus(String ticketId, TicketStatus status) throws SQLException {
        String sql = "UPDATE tickets SET status = ? WHERE ticket_id = ?";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setString(2, ticketId);
            stmt.executeUpdate();
        }
    }

    // Ticket silme
    public void deleteTicket(String ticketId) throws SQLException {
        String sql = "DELETE FROM tickets WHERE ticket_id = ?";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, ticketId);
            stmt.executeUpdate();
        }
    }

    //  Bir yetkilinin aldığı bütün ticketlar
    public List<Ticket> getTicketsByStaffUUID(String staffUUID) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE assigned_staff_uuid = ?";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, staffUUID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        }
        return tickets;
    }

    //  Bütün ticketlar
    public List<Ticket> getAllTickets() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        }
        return tickets;
    }

    //  Ticketın bütün alanlarını günceller
    public void updateTicket(Ticket ticket) throws SQLException {
        String sql = "UPDATE tickets SET creator_uuid = ?, category_id = ?, status = ?, description = ?, created_at = ?, assigned_staff_uuid = ?, assigned_at = ?, resolved_at = ? WHERE ticket_id = ?";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, ticket.getCreatorUUID().toString());
            stmt.setString(2, ticket.getCategory().getId());
            stmt.setString(3, ticket.getStatus().name());
            stmt.setString(4, ticket.getDescription());
            stmt.setString(5, ticket.getCreatedAt().toString());
            stmt.setString(6, ticket.getAssignedStaffUUID() != null ? ticket.getAssignedStaffUUID().toString() : null);
            stmt.setString(7, ticket.getAssignedAt() != null ? ticket.getAssignedAt().toString() : null);
            stmt.setString(8, ticket.getResolvedAt() != null ? ticket.getResolvedAt().toString() : null);
            stmt.setString(9, ticket.getTicketId());
            stmt.executeUpdate();
        }
    }

    // AssignedStaff ve AssignedAt güncelleme
    public void updateAssignedStaff(String ticketId, UUID staffUUID, LocalDateTime assignedAt) throws SQLException {
        String sql = "UPDATE tickets SET assigned_staff_uuid = ?, assigned_at = ? WHERE ticket_id = ?";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            if (staffUUID != null) {
                stmt.setString(1, staffUUID.toString());
            } else {
                stmt.setNull(1, Types.VARCHAR);
            }

            if (assignedAt != null) {
                stmt.setString(2, assignedAt.toString());
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }

            stmt.setString(3, ticketId);
            stmt.executeUpdate();
        }
    }

    // ResolvedAt ve Status güncelleme
    public void updateResolvedAt(String ticketId, LocalDateTime resolvedAt) throws SQLException {
        String sql = "UPDATE tickets SET resolved_at = ?, status = ? WHERE ticket_id = ?";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, resolvedAt.toString());
            stmt.setString(2, TicketStatus.RESOLVED.name());
            stmt.setString(3, ticketId);
            stmt.executeUpdate();
        }
    }

    //  Belli bir durumdaki bütün ticketları döndürür
    public List<Ticket> getTicketsByStatus(TicketStatus status) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();

        String sql = "SELECT * FROM tickets WHERE status = ?";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Ticket ticket = mapResultSetToTicket(rs);
                    tickets.add(ticket);
                }
            }
        }

        return tickets;
    }

    //  izleyici ekler
    public void addParticipant(String ticketId, UUID uuid) {
        String sql = "INSERT OR IGNORE INTO ticket_participants (ticket_id, participant_uuid) VALUES (?, ?)";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, ticketId);
            stmt.setString(2, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //  izleyici kaldırır
    public void removeParticipant(String ticketId, UUID uuid) {
        String sql = "DELETE FROM ticket_participants WHERE ticket_id = ? AND participant_uuid = ?";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, ticketId);
            stmt.setString(2, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //  izleyicileri döndürür
    public List<UUID> getParticipants(String ticketId) {
        List<UUID> participants = new ArrayList<>();
        String sql = "SELECT participant_uuid FROM ticket_participants WHERE ticket_id = ?";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, ticketId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    participants.add(UUID.fromString(rs.getString("participant_uuid")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participants;
    }

    //  oyuncu o ticketta izleyici mi ?
    public boolean isParticipant(String ticketId, UUID uuid) {
        String sql = "SELECT 1 FROM ticket_participants WHERE ticket_id = ? AND participant_uuid = ?";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, ticketId);
            stmt.setString(2, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Son olarak ResultSet'ten Ticket nesnesine dönüştürme metodu
    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        String ticketId = rs.getString("ticket_id");
        UUID creatorUUID = UUID.fromString(rs.getString("creator_uuid"));
        String categoryId = rs.getString("category_id");
        Category category = categoryManager.getCategory(categoryId); // categoryManager'dan alıyoruz
        LocalDateTime createdAt = LocalDateTime.parse(rs.getString("created_at"));
        String description = rs.getString("description");


        Ticket ticket = new Ticket(
                ticketId,
                creatorUUID,
                category,
                description,
                createdAt
        );

        TicketStatus status = TicketStatus.valueOf(rs.getString("status"));
        if (status != null) {
            ticket.setStatus(status);
        }

        String assignedStaffUUIDStr = rs.getString("assigned_staff_uuid");
        if (assignedStaffUUIDStr != null) {
            ticket.setAssignedStaffUUID(UUID.fromString(assignedStaffUUIDStr));
        }

        String assignedAt = rs.getString("assigned_at");
        if (assignedAt != null) {
            ticket.setAssignedAt(LocalDateTime.parse(assignedAt));
        }

        String resolvedAt = rs.getString("resolved_at");
        if (resolvedAt != null) {
            ticket.setResolvedAt(LocalDateTime.parse(resolvedAt));
        }

        return ticket;
    }

    //  Eklenecek ticket idyi döndürür
    public int getNextTicketNumberUsingCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM tickets";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") + 1;
            }
        }
        return 1;
    }

    //  Ticketı izleyenlerı döndürür
    private List<UUID> getParticipantsByTicketId(String ticketId) throws SQLException {
        List<UUID> participants = new ArrayList<>();
        String sql = "SELECT participant_uuid FROM ticket_participants WHERE ticket_id = ?";
        try (PreparedStatement stmt = connector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, ticketId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                participants.add(UUID.fromString(rs.getString("participant_uuid")));
            }
        }
        return participants;
    }
}
