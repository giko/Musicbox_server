package com.musicbox.server.packets.handlers;

import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets.Incoming;
import org.jetbrains.annotations.NotNull;
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
    public void HandlePacket(WebSocketConnection connection, @NotNull Incoming incoming) {
        if (incoming.getMessage() == null) {
            connection.close();
        }

        connections_.get(connection).getVkontakteClient().addSongToFavoriteByTrack(incoming.getMessage());
    }
}
