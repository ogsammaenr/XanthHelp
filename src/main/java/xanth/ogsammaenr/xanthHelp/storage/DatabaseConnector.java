package xanth.ogsammaenr.xanthHelp.storage;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnector {
    void connect() throws SQLException;

    void disconnect();

    Connection getConnection();

    void initializeTables() throws SQLException;
}
