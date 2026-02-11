package com.example.loginplugin.util;

import com.example.loginplugin.LoginPlugin;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TimeBarManager {

    private final LoginPlugin plugin;
    private final Map<UUID, BossBar> playerBars = new ConcurrentHashMap<>();
    private final Map<UUID, Long> joinTimes = new ConcurrentHashMap<>();
    private BukkitTask updateTask;

    public TimeBarManager(LoginPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (!isEnabled() || getDurationSeconds() <= 0) {
            return;
        }

        updateTask = plugin.getServer().getScheduler().runTaskTimer(plugin, this::updateBars, 0L, 20L);
    }

    public void stop() {
        if (updateTask != null) {
            updateTask.cancel();
            updateTask = null;
        }

        playerBars.values().forEach(BossBar::removeAll);
        playerBars.clear();
        joinTimes.clear();
    }

    public void startForPlayer(Player player) {
        if (!isEnabled() || getDurationSeconds() <= 0) {
            return;
        }

        UUID playerId = player.getUniqueId();
        joinTimes.put(playerId, System.currentTimeMillis());

        BossBar existingBar = playerBars.remove(playerId);
        if (existingBar != null) {
            existingBar.removeAll();
        }

        BossBar bossBar = Bukkit.createBossBar("", getBarColor(), getBarStyle());
        bossBar.addPlayer(player);
        playerBars.put(playerId, bossBar);

        updateBar(player, bossBar);
    }

    public void stopForPlayer(Player player) {
        UUID playerId = player.getUniqueId();
        joinTimes.remove(playerId);

        BossBar bossBar = playerBars.remove(playerId);
        if (bossBar != null) {
            bossBar.removePlayer(player);
            bossBar.removeAll();
        }
    }

    private void updateBars() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            BossBar bossBar = playerBars.get(player.getUniqueId());
            if (bossBar != null) {
                updateBar(player, bossBar);
            }
        }
    }

    private void updateBar(Player player, BossBar bossBar) {
        Long joinTime = joinTimes.get(player.getUniqueId());
        if (joinTime == null) {
            return;
        }

        int durationSeconds = getDurationSeconds();
        long elapsedSeconds = (System.currentTimeMillis() - joinTime) / 1000;
        long remainingSeconds = Math.max(0, durationSeconds - elapsedSeconds);

        double progress = Math.max(0.0, Math.min(1.0, (double) remainingSeconds / durationSeconds));
        bossBar.setProgress(progress);
        bossBar.setTitle(formatTitle(remainingSeconds));

        if (remainingSeconds <= 0) {
            stopForPlayer(player);
        }
    }

    private String formatTitle(long remainingSeconds) {
        String titleTemplate = plugin.getConfig().getString("plugin.time-bar.title", "§eLogin time left: §c%time%s");
        return titleTemplate.replace("%time%", String.valueOf(remainingSeconds));
    }

    private boolean isEnabled() {
        return plugin.getConfig().getBoolean("plugin.time-bar.enabled", false);
    }

    private int getDurationSeconds() {
        return plugin.getConfig().getInt("plugin.time-bar.duration", 60);
    }

    private BarColor getBarColor() {
        String colorValue = plugin.getConfig().getString("plugin.time-bar.color", "YELLOW");
        try {
            return BarColor.valueOf(colorValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BarColor.YELLOW;
        }
    }

    private BarStyle getBarStyle() {
        String styleValue = plugin.getConfig().getString("plugin.time-bar.style", "SOLID");
        try {
            return BarStyle.valueOf(styleValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BarStyle.SOLID;
        }
    }
}
