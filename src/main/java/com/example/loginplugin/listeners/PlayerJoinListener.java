package com.example.loginplugin.listeners;

import com.example.loginplugin.LoginPlugin;
import com.example.loginplugin.database.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerJoinListener implements Listener {

    private final LoginPlugin plugin;
    private final PlayerManager playerManager;

    public PlayerJoinListener(LoginPlugin plugin) {
        this.plugin = plugin;
        this.playerManager = new PlayerManager(plugin.getDatabaseManager());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.getTimeBarManager().startForPlayer(player);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                if (!playerManager.playerExists(player.getUniqueId())) {
                    player.sendMessage("§e[Login] You need to register! Use §c/register <password> <confirm-password>");
                } else {
                    player.sendMessage("§e[Login] Please login using §c/login <password>");
                }
            } catch (SQLException e) {
                player.sendMessage("§c[Login] An error occurred!");
                plugin.getLogger().severe("Error checking player: " + e.getMessage());
            }
        });
    }
}