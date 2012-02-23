package com.musicbox.server.packets.handlers;

import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import org.webbitserver.WebSocketConnection;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 23.02.12
 * Time: 10:52
 */
public class ChatMessage extends AbstractHandler {

    public ChatMessage(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(WebSocketConnection connection, Packets.Incoming incoming) {
        Packets.Outgoing packet = new Packets.Outgoing(Packets.Outgoing.Action.MESSAGE);
        packet.setMessage(connections.get(connection).getFirst_name().concat(" ")
                .concat(connections.get(connection).getLast_name())
                .concat(" написал: ").concat(incoming.getMessage()));
        server.broadcast(packet);
    }
}
