package xanth.ogsammaenr.xanthHelp.storage;

import xanth.ogsammaenr.xanthHelp.XanthHelp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnector implements DatabaseConnector {
    private Connection connection;
    private final XanthHelp plugin;

    public SQLiteConnector(XanthHelp plugin) {
        this.plugin = plugin;
    }

    @Override
    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String dbPath = plugin.getDataFolder().getAbsolutePath() + "/data.db";
            String url = "jdbc:sqlite:" + dbPath;
            connection = DriverManager.getConnection(url);

            plugin.getLogger().info("Connected to SQLite database");
        }
    }

    @Override
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                plugin.getLogger().info("SQLite disconnected.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void initializeTables() throws SQLException {
        String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS tickets (
                        ticket_id TEXT PRIMARY KEY,
                        creator_uuid TEXT NOT NULL,
                        category_id TEXT NOT NULL,
                        status TEXT NOT NULL,
                        description TEXT,
                        created_at TEXT NOT NULL,
                        assigned_staff_uuid TEXT,
                        assigned_at TEXT,
                        resolved_at TEXT
                    );
                """;
        String createParticipantTableSQL = """
                    CREATE TABLE IF NOT EXISTS ticket_participants (
                        ticket_id TEXT NOT NULL,
                        participant_uuid TEXT NOT NULL,
                        PRIMARY KEY (ticket_id, participant_uuid),
                        FOREIGN KEY (ticket_id) REFERENCES tickets(ticket_id) ON DELETE CASCADE
                    );
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            stmt.execute(createParticipantTableSQL);
        }
    }
}
