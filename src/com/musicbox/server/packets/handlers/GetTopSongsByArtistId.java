package com.musicbox.server.packets.handlers;

import com.musicbox.model.lastfm.structure.artist.Artist;
import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import com.musicbox.server.packets.outgoing.*;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 23.02.12
 * Time: 10:54
 */
public class GetTopSongsByArtistId extends AbstractHandler {

    public GetTopSongsByArtistId(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(@NotNull WebSocketConnection connection, @NotNull Packets.Incoming incoming) {
        ArtistSongsPacket packet = new ArtistSongsPacket();
        packet.setArtist(lfclient.getArtistInfoById(incoming.getMessage()));
        packet.setSongs(lfclient.getTopTracksByArtistID(incoming.getMessage()));
        packet.send(connection);
    }
}
