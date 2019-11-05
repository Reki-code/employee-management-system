package data;

import org.mariadb.jdbc.MariaDbPoolDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/ks?user=kierma&password=rikka0612";
    private MariaDbPoolDataSource pool;
    private static DatabaseHandler instance;

    private DatabaseHandler() {
        pool = new MariaDbPoolDataSource(DB_URL);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> pool.close()));
    }

    public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }


    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    private void printSQLException(SQLException e) {
        e.printStackTrace();
    }

    public static void main(String[] args) {
        var handler = getInstance();
    }
}
