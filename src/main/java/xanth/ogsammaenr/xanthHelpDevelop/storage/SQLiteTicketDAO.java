package xanth.ogsammaenr.xanthHelpDevelop.storage;

import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.manager.TicketCategoryManager;
import xanth.ogsammaenr.xanthHelpDevelop.model.Ticket;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * SQLite implementation of the {@link TicketDAO} interface.
 * <p>
 * This class manages all CRUD operations for {@link Ticket} objects in an SQLite database,
 * including mapping result sets to domain objects and handling associated participants in
 * the ticket_participants table.
 * </p>
 */
public class SQLiteTicketDAO implements TicketDAO {
    private final SQLiteConnector connector;
    private final XanthHelp plugin;
    private final TicketCategoryManager catManager;

    /**
     * Constructs a new SQLiteTicketDAO with the given connector.
     * Ensures that required tables exist.
     *
     * @param connector the SQLiteConnector providing database connections
     */
    public SQLiteTicketDAO(SQLiteConnector connector) {
        this.connector = connector;
        this.plugin = XanthHelp.getInstance();
        this.catManager = plugin.getTicketCategoryManager();
        createTableIfNotExists();
    }

    /**
     * Creates the "tickets" and "ticket_participants" tables if they do not already exist.
     */
    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS tickets ("
                     + "ticket_id TEXT PRIMARY KEY,"
                     + "creator_id TEXT NOT NULL,"
                     + "status TEXT NOT NULL,"
                     + "category_id TEXT NOT NULL,"
                     + "description TEXT,"
                     + "creation_date TEXT NOT NULL,"
                     + "staff_id TEXT,"
                     + "assignation_date TEXT,"
                     + "resolve_date TEXT"
                     + ");";
        try (Connection conn = connector.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // participants table
        String psql = "CREATE TABLE IF NOT EXISTS ticket_participants ("
                      + "ticket_id TEXT NOT NULL,"
                      + "participant_uuid TEXT NOT NULL,"
                      + "PRIMARY KEY(ticket_id, participant_uuid),"
                      + "FOREIGN KEY(ticket_id) REFERENCES tickets(ticket_id) ON DELETE CASCADE"
                      + ");";
        try (Connection conn = connector.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(psql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all tickets from the database, including their participants.
     *
     * @return a list of all {@link Ticket} objects
     */
    @Override
    public List<Ticket> findAll() {
        List<Ticket> list = new ArrayList<>();
        String sql = "SELECT * FROM tickets";
        try (Connection conn = connector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Ticket t = mapRow(rs);
                t.setParticipants(loadParticipants(t.getTicketId()));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Retrieves a single ticket by its ID, including its participants.
     *
     * @param ticketId the ID of the ticket to find
     * @return an Optional containing the {@link Ticket} if found, or empty if not
     */
    @Override
    public Optional<Ticket> findById(String ticketId) {
        String sql = "SELECT * FROM tickets WHERE ticket_id = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticketId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ticket t = mapRow(rs);
                    t.setParticipants(loadParticipants(ticketId));
                    return Optional.of(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Inserts a new ticket into the database and saves its participants.
     *
     * @param ticket the {@link Ticket} to insert
     */
    @Override
    public void insert(Ticket ticket) {
        String sql =
                "INSERT INTO tickets(ticket_id, creator_id, status, category_id, description, creation_date, staff_id, assignation_date, resolve_date) "
                + "VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticket.getTicketId());
            ps.setString(2, ticket.getCreatorId().toString());
            ps.setString(3, ticket.getStatus().name());
            ps.setString(4, ticket.getCategory().getId());
            ps.setString(5, ticket.getDescription());
            ps.setString(6, ticket.getCreationDate().toString());
            ps.setString(7, ticket.getStaffId() != null ? ticket.getStaffId().toString() : null);
            ps.setString(8, ticket.getAssignationDate() != null ? ticket.getAssignationDate().toString() : null);
            ps.setString(9, ticket.getResolveDate() != null ? ticket.getResolveDate().toString() : null);
            ps.executeUpdate();
            // participants
            saveParticipants(ticket);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing ticket and its participants in the database.
     *
     * @param ticket the {@link Ticket} to update
     */
    @Override
    public void update(Ticket ticket) {
        String sql =
                "UPDATE tickets SET creator_id=?, status=?, category_id=?, description=?, creation_date=?, staff_id=?, assignation_date=?, resolve_date=? WHERE ticket_id=?";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticket.getCreatorId().toString());
            ps.setString(2, ticket.getStatus().name());
            ps.setString(3, ticket.getCategory().getId());
            ps.setString(4, ticket.getDescription());
            ps.setString(5, ticket.getCreationDate().toString());
            ps.setString(6, ticket.getStaffId() != null ? ticket.getStaffId().toString() : null);
            ps.setString(7, ticket.getAssignationDate() != null ? ticket.getAssignationDate().toString() : null);
            ps.setString(8, ticket.getResolveDate() != null ? ticket.getResolveDate().toString() : null);
            ps.setString(9, ticket.getTicketId());
            ps.executeUpdate();
            // participants: delete old and re-insert
            deleteParticipants(ticket.getTicketId());
            saveParticipants(ticket);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a ticket and cascades deletion of its participants.
     *
     * @param ticketId the ID of the ticket to delete
     */
    @Override
    public void delete(String ticketId) {
        String sql = "DELETE FROM tickets WHERE ticket_id = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticketId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Maps the current row of the given ResultSet to a {@link Ticket.Builder} and builds a Ticket.
     *
     * @param rs the ResultSet positioned at a valid row
     * @return the mapped {@link Ticket}
     * @throws SQLException if an SQL error occurs
     */
    private Ticket mapRow(ResultSet rs) throws SQLException {
        return new Ticket.Builder(
                rs.getString("ticket_id"),
                UUID.fromString(rs.getString("creator_id")),
                TicketStatus.valueOf(rs.getString("status")),
                catManager.getCategoryById(rs.getString("category_id")),
                rs.getString("description"),
                LocalDateTime.parse(rs.getString("creation_date"))
        )
                .setStaffId(rs.getString("staff_id") != null ? UUID.fromString(rs.getString("staff_id")) : null)
                .setAssignationDate(rs.getString("assignation_date") != null ? LocalDateTime.parse(rs.getString("assignation_date")) : null)
                .setResolveDate(rs.getString("resolve_date") != null ? LocalDateTime.parse(rs.getString("resolve_date")) : null)
                .build();
    }

    /**
     * Loads participant UUIDs for the specified ticket ID.
     *
     * @param ticketId the ID of the ticket
     * @return a list of participant UUIDs
     */
    private List<UUID> loadParticipants(String ticketId) {
        List<UUID> list = new ArrayList<>();
        String sql = "SELECT participant_uuid FROM ticket_participants WHERE ticket_id = ?";
        try (Connection conn = connector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticketId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(UUID.fromString(rs.getString("participant_uuid")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Deletes all participants associated with the specified ticket ID.
     *
     * @param ticketId the ID of the ticket
     */
    private void deleteParticipants(String ticketId) {
        String sql = "DELETE FROM ticket_participants WHERE ticket_id = ?";
        try (Connection conn = connector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticketId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves participants of the given ticket into the database.
     * Uses batch insertion for efficiency.
     *
     * @param ticket the {@link Ticket} whose participants should be stored
     * @throws SQLException if a database access error occurs
     */
    private void saveParticipants(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO ticket_participants(ticket_id, participant_uuid) VALUES(?,?)";
        try (Connection conn = connector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (UUID uuid : ticket.getParticipants()) {
                ps.setString(1, ticket.getTicketId());
                ps.setString(2, uuid.toString());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
