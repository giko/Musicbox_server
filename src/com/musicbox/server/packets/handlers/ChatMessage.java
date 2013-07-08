package com.musicbox.server.packets.handlers;

import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import com.musicbox.server.packets.outgoing.MessagePacket;
import org.jetbrains.annotations.NotNull;
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
    public void HandlePacket(WebSocketConnection connection, @NotNull Packets.Incoming incoming) {
        server_.broadcast(new MessagePacket(connections_.get(connection).getFirst_name() + " " + connections_.get(connection).getLast_name() +
                " написал: " + incoming.getMessage()));
    }
}
