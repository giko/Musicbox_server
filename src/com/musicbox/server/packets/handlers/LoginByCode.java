package com.musicbox.server.packets.handlers;

import com.musicbox.model.LoginTokenEntity;
import com.musicbox.model.vkontakte.OAuthToken;
import com.musicbox.model.vkontakte.VkontakteClient;
import com.musicbox.server.Config;
import com.musicbox.server.MusicboxServer;
import com.musicbox.server.db.Connection;
import com.musicbox.server.logic.tools.MD5;
import com.musicbox.server.packets.Packets;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.WebSocketConnection;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 23.02.12
 * Time: 10:44
 */
public class LoginByCode extends AbstractHandler {


    public LoginByCode(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(@NotNull WebSocketConnection connection, @NotNull Packets.Incoming incoming) {
        String code = incoming.getMessage();
        OAuthToken oauth;
        if (!code.equals("")) {
            oauth = VkontakteClient.getOauthTokenByCode(code);
            if (oauth.getError() == null || oauth.getError().equals("")) {
                @NotNull Packets.Outgoing packet = new Packets.Outgoing(Packets.Outgoing.Action.TOKEN);

                String token = MD5.getMD5(oauth.getAccess_token()) +
                        MD5.getMD5(oauth.getUser_id() + UUID.randomUUID().toString());

                packet.setMessage(token);
                Session session = Connection.getSession();

                LoginTokenEntity loginTokenEntity = new LoginTokenEntity();
                loginTokenEntity.setoAuthToken(oauth);
                loginTokenEntity.setToken(token);

                session.save(loginTokenEntity);
                session.flush();
                session.close();

                logintokens_.put(token, oauth);
                connection.send(packet.toJson());
            } else {
                System.out.println("ERROR!" + oauth.getError() + oauth.getError_description());
            }
        } else {
            connection.send(new Packets.Outgoing(Packets.Outgoing.Action.REDIRECTTOVK, Config.getInstance().getVkappid()).toJson());
        }
    }
}
