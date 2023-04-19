package mc.obliviate.util.database.sql;

import java.sql.Connection;

public interface SQLDriverProvider {

    default boolean isConnected() {
        return getConnection() != null;
    }

    Connection getConnection();

    boolean connect();

    void disconnect();

}
