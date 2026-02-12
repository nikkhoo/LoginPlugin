package com.example.loginplugin.commands;

import com.example.loginplugin.LoginPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HideCommand implements CommandExecutor {

    private final LoginPlugin plugin;

    public HideCommand(LoginPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        plugin.getTimeBarManager().hideForPlayer(player);
        player.sendMessage("§e[Login] Time bar is now hidden.");

        return true;
    }
}
