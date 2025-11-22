package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    public static Connection getConnection() throws SQLException {
        String url = getDbUrl();
        String user = getDbUser();
        String password = getDbPassword();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found: " + e.getMessage());
            throw new SQLException("Failed to load JDBC driver", e);
        }
        System.out.println("Attempting database connection to: " + url);
        System.out.println("Using user: " + user);
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection successful!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private static String getDbUrl() {
        // Try to get pre-built URL first
        String url = System.getenv("DB_URL");
        if (url != null && !url.isEmpty()) {
            // If it's already a JDBC URL, use it as-is
            if (url.startsWith("jdbc:")) {
                return url;
            }
            // If it's a Railway-style URL (mysql://...), convert it
            if (url.startsWith("mysql://")) {
                // Format: mysql://user:password@host:port/database
                // Extract just host:port/database part and convert to JDBC
                String jdbcUrl = "jdbc:" + url;
                // Add SSL options if not already present
                if (!jdbcUrl.contains("useSSL")) {
                    jdbcUrl += "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
                }
                return jdbcUrl;
            }
        }

        // Build URL from individual components
        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String database = System.getenv("DB_NAME");

        if (host == null || port == null || database == null) {
            System.err.println("DB_HOST: " + host);
            System.err.println("DB_PORT: " + port);
            System.err.println("DB_NAME: " + database);
            throw new RuntimeException(
                    "Database configuration incomplete. Set DB_URL or (DB_HOST, DB_PORT, DB_NAME) environment variables.");
        }

        return "jdbc:mysql://" + host + ":" + port + "/" + database
                + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    }

    private static String getDbUser() {
        String user = System.getenv("DB_USER");
        if (user == null || user.isEmpty()) {
            throw new RuntimeException("Database user not configured. Set DB_USER environment variable.");
        }
        return user;
    }

    private static String getDbPassword() {
        String password = System.getenv("DB_PASSWORD");
        if (password == null || password.isEmpty()) {
            throw new RuntimeException("Database password not configured. Set DB_PASSWORD environment variable.");
        }
        return password;
    }
}
