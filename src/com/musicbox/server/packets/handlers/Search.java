package com.musicbox.server.packets.handlers;

import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import org.webbitserver.WebSocketConnection;

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
    public void HandlePacket(WebSocketConnection connection, Packets.Incoming incoming) {
        Packets.Outgoing packet = new Packets.Outgoing(Packets.Outgoing.Action.SEARCHRESULT);
        if (incoming.getMessage().equals("")) {
            packet.setArtists(lfclient.getTopArtists());
        } else {
            packet.setSongs(lfclient.SearchTrack(incoming.getMessage().trim()));
            packet.setArtists(lfclient.SearchArtist(incoming.getMessage().trim()));
            packet.setTags(lfclient.SearchTag(incoming.getMessage()));
        }
        connection.send(packet.toJson());
    }
}
