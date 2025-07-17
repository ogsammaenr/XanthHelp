package xanth.ogsammaenr.xanthHelpDevelop.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector {
    private final String url;

    /**
     * Constructs a new connector using the given file path.
     *
     * @param databaseFilePath Path to the SQLite database file
     */
    public SQLiteConnector(String databaseFilePath) {
        // JDBC URL for SQLite: jdbc:sqlite:<path>
        this.url = "jdbc:sqlite:" + databaseFilePath;
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to load SQLite JDBC driver", e);
        }
    }

    /**
     * Opens and returns a new Connection to the SQLite database.
     * Caller is responsible for closing the connection.
     *
     * @return new Connection
     * @throws SQLException if a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
