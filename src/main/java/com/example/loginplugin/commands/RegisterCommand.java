package com.example.loginplugin.commands;

import com.example.loginplugin.LoginPlugin;
import com.example.loginplugin.database.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class RegisterCommand implements CommandExecutor {

    private final LoginPlugin plugin;
    private final PlayerManager playerManager;

    public RegisterCommand(LoginPlugin plugin) {
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

        if (args.length != 2) {
            player.sendMessage("§cUsage: /register <password> <confirm-password>");
            return true;
        }

        String password = args[0];
        String confirmPassword = args[1];

        if (password.length() < 6) {
            player.sendMessage("§c[Login] Password must be at least 6 characters long!");
            return true;
        }

        if (!password.equals(confirmPassword)) {
            player.sendMessage("§c[Login] Passwords do not match!");
            return true;
        }

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                if (playerManager.playerExists(player.getUniqueId())) {
                    player.sendMessage("§c[Login] Your account is already registered!");
                    return;
                }

                if (playerManager.registerPlayer(player.getUniqueId(), player.getName(), password)) {
                    player.sendMessage("§a[Login] Account registered successfully! Use /login to login.");
                } else {
                    player.sendMessage("§c[Login] Registration failed! Username might be taken.");
                }
            } catch (SQLException e) {
                player.sendMessage("§c[Login] An error occurred during registration!");
                plugin.getLogger().severe("Registration error: " + e.getMessage());
            }
        });

        return true;
    }
}