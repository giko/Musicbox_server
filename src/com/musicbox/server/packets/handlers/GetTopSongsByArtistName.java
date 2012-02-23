package com.musicbox.server.packets.handlers;

import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import org.webbitserver.WebSocketConnection;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 23.02.12
 * Time: 10:55
 */
public class GetTopSongsByArtistName extends AbstractHandler{

    public GetTopSongsByArtistName(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(WebSocketConnection connection, Packets.Incoming incoming) {
        Packets.Outgoing packet = new Packets.Outgoing(Packets.Outgoing.Action.SONGS);
        packet.setSongs(lfclient.getTopTracksByArtistName(incoming.getMessage()));
        connection.send(packet.toJson());
    }
}
