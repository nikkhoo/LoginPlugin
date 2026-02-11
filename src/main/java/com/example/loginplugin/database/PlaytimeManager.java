// PlaytimeManager.java

package com.example.loginplugin.database;

import java.util.HashMap;
import java.util.Map;

public class PlaytimeManager {
    private Map<String, Long> playtimeData;

    public PlaytimeManager() {
        playtimeData = new HashMap<>();
    }

    public void logPlaytime(String playerId, long duration) {
        playtimeData.put(playerId, playtimeData.getOrDefault(playerId, 0L) + duration);
    }

    public long getPlaytime(String playerId) {
        return playtimeData.getOrDefault(playerId, 0L);
    }

    public void resetPlaytime(String playerId) {
        playtimeData.put(playerId, 0L);
    }
}