package com.musicbox.server.packets.handlers;

import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import com.musicbox.vkontakte.VkontakteClient;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.WebSocketConnection;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 29.02.12
 * Time: 22:53
 */
public class GetAudioByTrack extends AbstractHandler {
    public GetAudioByTrack(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(@NotNull WebSocketConnection connection, @NotNull Packets.Incoming incoming) {
        @NotNull VkontakteClient vkclient = new VkontakteClient(connections.get(connection).getToken());
        @NotNull Packets.Outgoing packet = new Packets.Outgoing(Packets.Outgoing.Action.AUDIO);
        packet.setAudio(vkclient.getAudioByTrack(incoming.getMessage()));
        connection.send(packet.toJson());
    }
}
