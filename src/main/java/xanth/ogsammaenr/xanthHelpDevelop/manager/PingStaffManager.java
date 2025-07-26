package xanth.ogsammaenr.xanthHelpDevelop.manager;

import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PingStaffManager {
    private Map<UUID, Long> waitingPlayers;
    private XanthHelp plugin;

    private long waitingTime;

    public PingStaffManager() {
        this.plugin = XanthHelp.getInstance();
        this.waitingPlayers = new HashMap<UUID, Long>();
    }

    public void putPlayer(UUID playerId) {
        waitingPlayers.put(playerId, System.currentTimeMillis());
    }

    public void removePlayer(UUID playerId) {
        waitingPlayers.remove(playerId);
    }

    public long getWaitingTime(UUID playerId) {
        return waitingPlayers.get(playerId);
    }

    public String getCooldown(UUID playerId) {
        Long startTime = waitingPlayers.get(playerId);
        if (startTime == null) {
            return "0m";
        }

        long now = System.currentTimeMillis();
        long endTime = startTime + waitingTime;
        long remainingMillis = endTime - now;

        if (remainingMillis <= 0) {
            return "0m";
        }

        long totalMinutes = remainingMillis / 1000 / 60;
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }

    public boolean isPlayerWaiting(UUID playerId) {
        if (waitingPlayers.containsKey(playerId)) return false;
        if (waitingPlayers.get(playerId) + waitingTime < System.currentTimeMillis()) {
            waitingPlayers.remove(playerId);
            return false;
        }
        return true;
    }

    public Map<UUID, Long> getWaitingPlayers() {
        return waitingPlayers;
    }
}
