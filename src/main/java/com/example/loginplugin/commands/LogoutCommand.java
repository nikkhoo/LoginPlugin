package com.example.loginplugin.commands;

import com.example.loginplugin.LoginPlugin;
import com.example.loginplugin.database.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class LogoutCommand implements CommandExecutor {

    private final LoginPlugin plugin;
    private final PlayerManager playerManager;

    public LogoutCommand(LoginPlugin plugin) {
        this.plugin = plugin;
        this.playerManager = new PlayerManager(plugin.getDatabaseManager());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                playerManager.setLoggedIn(player.getUniqueId(), false);
                player.sendMessage("§a[Login] You have been logged out!");
            } catch (SQLException e) {
                player.sendMessage("§c[Login] An error occurred during logout!");
                plugin.getLogger().severe("Logout error: " + e.getMessage());
            }
        });

        return true;
    }
}