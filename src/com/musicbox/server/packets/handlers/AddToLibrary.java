package com.musicbox.server.packets.handlers;

import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import org.webbitserver.WebSocketConnection;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 23.02.12
 * Time: 19:36
 */
public class AddToLibrary extends AbstractHandler {

    public AddToLibrary(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(WebSocketConnection connection, Packets.Incoming incoming) {
        connections.get(connection).getVkontakteClient().addSongToFavoriteByTrack(incoming.getMessage());
    }
}
