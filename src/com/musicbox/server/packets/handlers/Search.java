package com.musicbox.server.packets.handlers;

import com.musicbox.model.lastfm.structure.artist.Artist;
import com.musicbox.model.lastfm.structure.tag.Tag;
import com.musicbox.model.lastfm.structure.track.Track;
import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.WebSocketConnection;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 23.02.12
 * Time: 10:47
 */
public class Search extends AbstractHandler {

    public Search(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(@NotNull WebSocketConnection connection, @NotNull Packets.Incoming incoming) {
        @NotNull Packets.Outgoing packet = new Packets.Outgoing(Packets.Outgoing.Action.SEARCHRESULT);
        if (incoming.getMessage().equals("")) {
            packet.setArtists(lfclient.getTopArtists());
        } else {
            List<Track> tracks = lfclient.SearchTrack(incoming.getMessage().trim());
            List<Artist> artists = lfclient.SearchArtist(incoming.getMessage().trim());
            List<Tag> tags = lfclient.SearchTag(incoming.getMessage().trim());
            if (tracks != null) {
                packet.setSongs(tracks);
            }
            if (artists != null) {
                packet.setArtists(artists);
            }
            if (tags != null) {
                packet.setTags(tags);
            }
        }
        connection.send(packet.toJson());
    }
}
