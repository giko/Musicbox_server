package com.musicbox.server.packets.handlers;

import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import com.musicbox.server.packets.Packets.Incoming;
import com.musicbox.vkontakte.VkontakteClient;
import org.webbitserver.WebSocketConnection;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 23.02.12
 * Time: 10:07
 */
public class Login extends AbstractHandler {

    public Login(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(WebSocketConnection connection, Incoming incoming) {
        String token = incoming.getMessage();
        if (token != null && logintokens.containsKey(token)) {
            connection.data(USERNAME_KEY, logintokens.get(token).getUser_id());
            VkontakteClient vkclient = new VkontakteClient(logintokens.get(token));
            connections.put(connection, vkclient.getProfile());
            Packets.Outgoing packet = new Packets.Outgoing(Packets.Outgoing.Action.LOGINSUCCESS);
            connection.send(packet.toJson());
        } else {
            connection.send(new Packets.Outgoing(Packets.Outgoing.Action.REDIRECTTOVK).toJson());
        }
    }
}
