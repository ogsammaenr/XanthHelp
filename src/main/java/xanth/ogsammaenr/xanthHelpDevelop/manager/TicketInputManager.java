package xanth.ogsammaenr.xanthHelpDevelop.manager;

import org.bukkit.entity.Player;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TicketInputManager {
    private final Map<UUID, TicketPendingInput> waitingPlayers = new HashMap<>();

    private final long TIMEOUT_MILLIS = 15 * 1000;


    public void waitForInput(UUID playerId, TicketCategory category) {
        waitingPlayers.put(playerId, new TicketPendingInput(category, System.currentTimeMillis()));
    }

    public boolean isWaiting(UUID playerId) {
        return waitingPlayers.containsKey(playerId);
    }

    public long getStartTimeMillisForPlayer(UUID playerId) {
        return waitingPlayers.get(playerId).startTimeMillis();
    }

    public TicketCategory getCategoryForPlayer(UUID playerId) {
        return waitingPlayers.get(playerId).category();
    }

    public TicketPendingInput getPendingInput(UUID playerId) {
        return waitingPlayers.get(playerId);
    }

    public void cancelWaiting(UUID playerId) {
        waitingPlayers.remove(playerId);
    }

    public boolean isExpired(Player player) {
        TicketPendingInput input = waitingPlayers.get(player.getUniqueId());
        if (input == null) return true;
        return System.currentTimeMillis() - input.startTimeMillis > TIMEOUT_MILLIS;
    }

    public TicketCategory consumeCategory(Player player) {
        TicketPendingInput input = waitingPlayers.remove(player.getUniqueId());
        return input != null ? input.category() : null;
    }


    public record TicketPendingInput(TicketCategory category, long startTimeMillis) {
    }
}
