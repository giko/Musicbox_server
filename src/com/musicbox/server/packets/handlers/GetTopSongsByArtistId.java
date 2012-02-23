package com.musicbox.server.packets.handlers;

import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import org.webbitserver.WebSocketConnection;

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
    public void HandlePacket(WebSocketConnection connection, Packets.Incoming incoming) {
        Packets.Outgoing packet = new Packets.Outgoing(Packets.Outgoing.Action.SEARCHRESULT);
        packet.setSongs(lfclient.getTopTracksByArtistID(incoming.getMessage()));
        connection.send(packet.toJson());
    }
}
