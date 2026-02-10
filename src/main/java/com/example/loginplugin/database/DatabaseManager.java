package com.example.loginplugin.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

public class DatabaseManager {

    private HikariDataSource dataSource;
    private final JavaPlugin plugin;

    public DatabaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect() throws SQLException {
        String host = plugin.getConfig().getString("database.host", "localhost");
        int port = plugin.getConfig().getInt("database.port", 3306);
        String database = plugin.getConfig().getString("database.database", "minecraft");
        String user = plugin.getConfig().getString("database.user", "root");
        String password = plugin.getConfig().getString("database.password", "");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        dataSource = new HikariDataSource(config);
    }

    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public void createTables() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS `players` (" +
                    "`id` INT AUTO_INCREMENT PRIMARY KEY," +
                    "`uuid` VARCHAR(36) UNIQUE NOT NULL," +
                    "`username` VARCHAR(16) UNIQUE NOT NULL," +
                    "`password_hash` VARCHAR(60) NOT NULL," +
                    "`last_login` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                    "`is_logged_in` BOOLEAN DEFAULT FALSE," +
                    "`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";

            stmt.executeUpdate(createTableSQL);
            plugin.getLogger().info("✓ Players table created or already exists");
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}