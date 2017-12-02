package io.veron.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    private final String connectionString;
    private final Properties props;
    private Connection connection;

    Database(String url, int port, String database, Properties props) {
        this.connectionString = String.format("jdbc:postgresql://%1$s:%2$s/%3$s", url, port, database);
        this.props = props;
    }

    public void connect() throws SQLException {
        if (!this.isConnected())
            this.connection = DriverManager.getConnection(connectionString, props);
    }

    public void disconnect() throws SQLException {
        if (this.isConnected())
            this.connection.close();
    }

    public boolean isConnected() throws SQLException {
        return (this.connection == null || this.connection.isClosed());
    }
}
