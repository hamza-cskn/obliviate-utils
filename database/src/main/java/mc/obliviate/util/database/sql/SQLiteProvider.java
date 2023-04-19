package mc.obliviate.util.database.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteProvider implements SQLDriverProvider {

    private static Connection connection = null;

    private final String filePath;

    public SQLiteProvider(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public boolean connect() {
        connection = null;
        try {

            Class.forName("org.sqlite.JDBC");
            final String URL = "jdbc:sqlite:" + filePath;

            connection = DriverManager.getConnection(URL);
            if (connection == null)
                throw new RuntimeException("Could not connected to SQLite database!");
        } catch (SQLException | ClassNotFoundException | RuntimeException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void disconnect() {
        if (connection == null) return;
        try {
            connection.close();
            connection = null;
        } catch (final SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
