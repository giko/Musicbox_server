package com.musicbox.server.packets.handlers;

import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import com.musicbox.server.packets.outgoing.SearchResultPacket;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.WebSocketConnection;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 08.05.12
 * Time: 10:32
 */
public class SearchSimilarArtistsByName extends AbstractHandler {


    public SearchSimilarArtistsByName(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(@NotNull WebSocketConnection connection, @NotNull Packets.Incoming incoming) {
        SearchResultPacket packet = new SearchResultPacket();
        packet.setArtists(lfclient.getSimilarArtistsByName(incoming.getMessage()));
        packet.send(connection);
    }
}