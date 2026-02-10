package com.example.loginplugin.database;

import com.example.loginplugin.util.PasswordUtil;
import java.sql.*;
import java.util.UUID;

public class PlayerManager {

    private final DatabaseManager databaseManager;

    public PlayerManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Register a new player account
     */
    public boolean registerPlayer(UUID uuid, String username, String password) throws SQLException {
        String hashedPassword = PasswordUtil.hashPassword(password);

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO players (uuid, username, password_hash) VALUES (?, ?, ?)")) {

            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, username);
            pstmt.setString(3, hashedPassword);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry
                return false;
            }
            throw e;
        }
    }

    /**
     * Check if a player account exists
     */
    public boolean playerExists(UUID uuid) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT id FROM players WHERE uuid = ?")) {

            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    /**
     * Authenticate a player
     */
    public boolean authenticatePlayer(UUID uuid, String password) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT password_hash FROM players WHERE uuid = ?")) {

            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                return PasswordUtil.verifyPassword(password, storedHash);
            }
            return false;
        }
    }

    /**
     * Mark a player as logged in
     */
    public void setLoggedIn(UUID uuid, boolean loggedIn) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE players SET is_logged_in = ? WHERE uuid = ?")) {

            pstmt.setBoolean(1, loggedIn);
            pstmt.setString(2, uuid.toString());
            pstmt.executeUpdate();
        }
    }

    /**
     * Check if a player is logged in
     */
    public boolean isLoggedIn(UUID uuid) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT is_logged_in FROM players WHERE uuid = ?")) {

            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("is_logged_in");
            }
            return false;
        }
    }
}