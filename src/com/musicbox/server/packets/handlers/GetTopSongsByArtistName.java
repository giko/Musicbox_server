package com.musicbox.server.packets.handlers;

import com.musicbox.lastfm.structure.artist.Artist;
import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 23.02.12
 * Time: 10:55
 */
public class GetTopSongsByArtistName extends AbstractHandler {

    public GetTopSongsByArtistName(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(@NotNull WebSocketConnection connection, @NotNull Packets.Incoming incoming) {
        @NotNull Packets.Outgoing packet = new Packets.Outgoing(Packets.Outgoing.Action.SEARCHRESULT);
        packet.setSongs(lfclient.getTopTracksByArtistName(incoming.getMessage()));
        ArrayList<Artist> artist = new ArrayList<Artist>();
        artist.add(lfclient.getArtistInfoByName(incoming.getMessage()));
        packet.setArtists(artist);
        connection.send(packet.toJson());
    }
}
