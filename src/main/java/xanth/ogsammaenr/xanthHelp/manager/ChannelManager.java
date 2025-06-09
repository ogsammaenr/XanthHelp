package xanth.ogsammaenr.xanthHelp.manager;

import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.model.TicketChatChannel;

import java.util.*;

public class ChannelManager {
    private final XanthHelp plugin;

    private final Map<String, TicketChatChannel> channels;

    public ChannelManager(XanthHelp plugin) {
        this.plugin = plugin;
        this.channels = new HashMap<>();
    }

    ///     Yeni bir ticket için sohbet kanalı oluşturur
    public void createChannel(String ticketId, UUID owner, UUID staff) {
        if (!channels.containsKey(ticketId)) {
            channels.put(ticketId, new TicketChatChannel(ticketId, owner, staff));
        }
    }

    ///     Oyuncunun belirli bir sohbet kanalına erişmesini sağlar
    public boolean joinChannel(String ticketId, UUID player) {
        TicketChatChannel channel = channels.get(ticketId);
        if (channel == null) return false;

        if (channel.isParticipant(player)) return true;

        if (channel.isAccessApproved(player)) {
            channel.joinChannel(player);
            return true;
        }
        return false;
    }

    ///     Oyuncu belirli bir sohbet kanalına erişim talebi gönderir
    public boolean requestChannel(String ticketId, UUID requester) {
        TicketChatChannel channel = channels.get(ticketId);
        if (channel == null || channel.isParticipant(requester)) return false;

        if (!channel.hasRequested(requester)) {
            channel.requestAccess(requester);
            return true;
        }

        return false;
    }

    ///     Ticket yetkilisi başka bir yetkilinin erişim talebini kabul eder
    public boolean approveRequest(String ticketId, UUID approver, UUID requester) {
        TicketChatChannel channel = channels.get(ticketId);
        if (channel == null) return false;

        if (Objects.equals(channel.getAssignedStaff(), approver)) return false;

        if (channel.hasRequested(requester)) {
            channel.approveRequest(requester);
            return true;
        }

        return false;
    }

    ///     Oyuncu belirtilen sohbet kanalına erişebiliyor mu
    public boolean isParticipant(String ticketId, UUID player) {
        TicketChatChannel channel = channels.get(ticketId);
        return channel != null && channel.isParticipant(player);
    }

    ///     Belli bir kanaldaki üyeleri döner
    public Set<UUID> getParticipants(String ticketId) {
        TicketChatChannel channel = channels.get(ticketId);
        return channel != null ? channel.getActiveParticipants() : Collections.emptySet();
    }

    ///     Oyuncunun erişebildiği bütün sohbet kanallarını döndürür
    public List<String> getChannelsFor(UUID player) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, TicketChatChannel> entry : channels.entrySet()) {
            if (entry.getValue().isParticipant(player)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    ///     Oyuncunun bağlı olduğu kanalı döner
    public String getChannelOf(UUID playerId) {
        for (Map.Entry<String, TicketChatChannel> entry : channels.entrySet()) {
            if (entry.getValue().isParticipant(playerId)) {
                return entry.getKey();
            }
        }
        return null;
    }

    ///     Belirli bir ticketın sohbet kanalını döner
    public TicketChatChannel getChannel(String ticketId) {
        return channels.get(ticketId);
    }

    ///     Belirli bir sohbet kanalını siler
    public void removeChannel(String ticketId) {
        channels.remove(ticketId);
    }
}


