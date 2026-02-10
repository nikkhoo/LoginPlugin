package com.example.loginplugin;

import com.example.loginplugin.database.DatabaseManager;
import com.example.loginplugin.listeners.PlayerJoinListener;
import com.example.loginplugin.listeners.PlayerQuitListener;
import com.example.loginplugin.commands.LoginCommand;
import com.example.loginplugin.commands.RegisterCommand;
import com.example.loginplugin.commands.LogoutCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class LoginPlugin extends JavaPlugin {

    private static LoginPlugin instance;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        instance = this;

        // Save default config if it doesn't exist
        saveDefaultConfig();

        // Initialize database
        try {
            databaseManager = new DatabaseManager(this);
            databaseManager.connect();
            databaseManager.createTables();
            getLogger().info("✓ Database connected successfully!");
        } catch (Exception e) {
            getLogger().severe("✗ Failed to connect to database!");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        // Register commands
        getCommand("login").setExecutor(new LoginCommand(this));
        getCommand("register").setExecutor(new RegisterCommand(this));
        getCommand("logout").setExecutor(new LogoutCommand(this));

        getLogger().info("✓ LoginPlugin enabled!");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.disconnect();
        }
        getLogger().info("✓ LoginPlugin disabled!");
    }

    public static LoginPlugin getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}