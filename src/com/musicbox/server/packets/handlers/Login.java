package com.musicbox.server.packets.handlers;

import com.musicbox.server.Config;
import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import com.musicbox.vkontakte.VkontakteClient;
import com.musicbox.vkontakte.structure.profiles.Profile;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.WebSocketConnection;

import java.util.Iterator;

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
    public void HandlePacket(@NotNull WebSocketConnection connection, @NotNull Packets.Incoming incoming) {
        String token = incoming.getMessage();
        if (token != null && logintokens_.containsKey(token)) {
            connection.data(server_.USERNAME_KEY, logintokens_.get(token).getUser_id());
            @NotNull VkontakteClient vkclient = new VkontakteClient(logintokens_.get(token));

            boolean exists = false;

            Iterator<Profile> it = connections_.values().iterator();

            while (it.hasNext()) {
                Profile current_profile = it.next();

                if (current_profile != null && current_profile.getUid() == vkclient.getProfile().getUid()) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                connections_.put(connection, vkclient.getProfile());
                Packets.Outgoing packet = new Packets.Outgoing(Packets.Outgoing.Action.LOGINSUCCESS);
                connection.send(packet.toJson());
            } else {
                connection.send(new Packets.Outgoing(Packets.Outgoing.Action.CRITICALERROR, "Already Logged in").toJson());
            }
        } else {
            connection.send(new Packets.Outgoing(Packets.Outgoing.Action.REDIRECTTOVK, Config.getInstance().getVkappid()).toJson());
        }
    }
}
