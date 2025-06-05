package xanth.ogsammaenr.xanthHelp.storage;

import org.bukkit.configuration.ConfigurationSection;
import xanth.ogsammaenr.xanthHelp.XanthHelp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnector implements DatabaseConnector {
    private Connection connection;

    private final XanthHelp plugin;

    public MySQLConnector(XanthHelp plugin) {
        this.plugin = plugin;
    }

    @Override
    public void connect() throws SQLException {
        ConfigurationSection mysql = plugin.getConfig().getConfigurationSection("storage.mysql");
        if (mysql == null) {
            plugin.getLogger().severe("MySQL configuration missing!");
            return;
        }
        String host = mysql.getString("host");
        int port = mysql.getInt("port");
        String dbName = mysql.getString("database");
        String user = mysql.getString("user");
        String password = mysql.getString("password");


        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false";
        connection = DriverManager.getConnection(url, user, password);
        plugin.getLogger().info("Connected to MySQL database!");
    }

    @Override
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                plugin.getLogger().info("MySQL disconnected.");
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
                        ticket_id VARCHAR(36) PRIMARY KEY,
                        creator_uuid VARCHAR(36) NOT NULL,
                        category_id VARCHAR(50) NOT NULL,
                        status VARCHAR(20) NOT NULL,
                        description TEXT,
                        created_at VARCHAR(30) NOT NULL,
                        assigned_staff_uuid VARCHAR(36),
                        assigned_at VARCHAR(30),
                        resolved_at VARCHAR(30)
                    );
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }
}
