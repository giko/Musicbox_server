package com.musicbox.server.packets.handlers;

import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import com.musicbox.vkontakte.VkontakteClient;
import org.webbitserver.WebSocketConnection;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 23.02.12
 * Time: 10:50
 */
public class GetUrlByTrack extends AbstractHandler{

    public GetUrlByTrack(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(WebSocketConnection connection, Packets.Incoming incoming) {
        VkontakteClient vkclient = new VkontakteClient(connections.get(connection).getToken());
        Packets.Outgoing packet = new Packets.Outgoing(Packets.Outgoing.Action.SONGURL);
        packet.setMessage(vkclient.getURLByTrack(incoming.getMessage()));
        connection.send(packet.toJson());
    }
}
