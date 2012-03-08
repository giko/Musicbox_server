package com.musicbox.server.packets.handlers;

import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import com.musicbox.server.packets.Packets.Outgoing;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.WebSocketConnection;

/**
 * Created by IntelliJ IDEA.
 * User: Giko
 * Date: 24.02.12
 * Time: 10:36
 * To change this template use File | Settings | File Templates.
 */
public class SearchByTag extends AbstractHandler {


    public SearchByTag(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(@NotNull WebSocketConnection connection, @NotNull Packets.Incoming incoming) {
        @NotNull Outgoing packet = new Outgoing(Outgoing.Action.SEARCHRESULT);
        packet.setArtists(lfclient.SearchArtistByTag(incoming.getMessage()));
        connection.send(packet.toJson());
    }
}
