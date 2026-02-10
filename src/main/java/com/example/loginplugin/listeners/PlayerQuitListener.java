package com.example.loginplugin.listeners;

import com.example.loginplugin.LoginPlugin;
import com.example.loginplugin.database.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class PlayerQuitListener implements Listener {

    private final LoginPlugin plugin;
    private final PlayerManager playerManager;

    public PlayerQuitListener(LoginPlugin plugin) {
        this.plugin = plugin;
        this.playerManager = new PlayerManager(plugin.getDatabaseManager());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                playerManager.setLoggedIn(player.getUniqueId(), false);
            } catch (SQLException e) {
                plugin.getLogger().severe("Error logging out player: " + e.getMessage());
            }
        });
    }
}