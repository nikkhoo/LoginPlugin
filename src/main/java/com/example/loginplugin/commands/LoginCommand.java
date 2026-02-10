package com.example.loginplugin.commands;

import com.example.loginplugin.LoginPlugin;
import com.example.loginplugin.database.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class LoginCommand implements CommandExecutor {

    private final LoginPlugin plugin;
    private final PlayerManager playerManager;

    public LoginCommand(LoginPlugin plugin) {
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

        if (args.length != 1) {
            player.sendMessage("§cUsage: /login <password>");
            return true;
        }

        String password = args[0];

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                if (!playerManager.playerExists(player.getUniqueId())) {
                    player.sendMessage("§c[Login] Your account doesn't exist! Use /register to create one.");
                    return;
                }

                if (playerManager.authenticatePlayer(player.getUniqueId(), password)) {
                    playerManager.setLoggedIn(player.getUniqueId(), true);
                    player.sendMessage("§a[Login] You have been logged in successfully!");
                } else {
                    player.sendMessage("§c[Login] Incorrect password!");
                }
            } catch (SQLException e) {
                player.sendMessage("§c[Login] An error occurred during login!");
                plugin.getLogger().severe("Login error: " + e.getMessage());
            }
        });

        return true;
    }
}