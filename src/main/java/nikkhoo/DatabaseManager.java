// Updated DatabaseManager.java to add playtime tracking columns

// Assuming the existing class starts here
public class DatabaseManager {
    // Existing columns

    // New columns for playtime tracking
    private Timestamp session_start;
    private int playtime_today;
    private Timestamp last_reset;

    // Constructor
    public DatabaseManager() {
        // Initialize new columns if needed
        this.playtime_today = 0;
        this.session_start = new Timestamp(System.currentTimeMillis());
        this.last_reset = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters for new columns
    public Timestamp getSessionStart() {
        return session_start;
    }

    public void setSessionStart(Timestamp session_start) {
        this.session_start = session_start;
    }

    public int getPlaytimeToday() {
        return playtime_today;
    }

    public void setPlaytimeToday(int playtime_today) {
        this.playtime_today = playtime_today;
    }

    public Timestamp getLastReset() {
        return last_reset;
    }

    public void setLastReset(Timestamp last_reset) {
        this.last_reset = last_reset;
    }

    // Additional logic for handling playtime tracking can be added here
}