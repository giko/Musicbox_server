package com.musicbox.server.packets.handlers;

import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import com.musicbox.server.packets.outgoing.SearchResultPacket;
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
        SearchResultPacket packet = new SearchResultPacket();
        packet.setArtists(lfclient.SearchArtistByTag(incoming.getMessage()));
        packet.send(connection);
    }
}
