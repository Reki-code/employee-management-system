package data;

import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.prefs.Preferences;

public class DatabaseHandler {
    private static final String DEFAULT_HOST_NAME = "localhost";
    private static final String DEFAULT_HOST_PORT = "3306";
    private static final String DEFAULT_USER = "kierma";
    private static final String DEFAULT_PASS = "rikka0612";
    private MariaDbPoolDataSource pool;

    private DatabaseHandler() {
        var prefs = Preferences.userNodeForPackage(DatabaseHandler.class);
        var hostName = prefs.get("host_name", DEFAULT_HOST_NAME);
        var hostPort = prefs.get("host_port", DEFAULT_HOST_PORT);
        var user = prefs.get("user", DEFAULT_USER);
        var pass = prefs.get("pass", DEFAULT_PASS);
        String dbUrl = "jdbc:mariadb://" + hostName + ":" + hostPort + "/ks?user=" + user + "&password=" + pass;
        pool = new MariaDbPoolDataSource(dbUrl);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> pool.close()));
    }

    /**
     * 获取实例
     *
     * @return DatabaseHandler 实例
     */
    static DatabaseHandler getInstance() {
        return BillPughSingleton.INSTANCE;
    }

    /**
     * 从连接池中获取连接
     *
     * @return 和数据库的连接
     * @throws SQLException 连接错误
     */
    Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    /**
     * BillPugh 单例模式, 线程安全
     */
    private static class BillPughSingleton {
        private static final DatabaseHandler INSTANCE = new DatabaseHandler();
    }

    private void printSQLException(SQLException e) {
        e.printStackTrace();
    }

    public static String getDefaultHostName() {
        return DEFAULT_HOST_NAME;
    }

    public static String getDefaultHostPort() {
        return DEFAULT_HOST_PORT;
    }

    public static String getDefaultUser() {
        return DEFAULT_USER;
    }

    public static String getDefaultPass() {
        return DEFAULT_PASS;
    }
}
