package com.musicbox.server.packets.handlers;

import com.musicbox.model.LoginTokenEntity;
import com.musicbox.model.UserEntity;
import com.musicbox.model.vkontakte.OAuthToken;
import com.musicbox.model.vkontakte.VkontakteClient;
import com.musicbox.server.Config;
import com.musicbox.server.MusicboxServer;
import com.musicbox.server.db.Connection;
import com.musicbox.server.logic.tools.MD5;
import com.musicbox.server.packets.Packets;
import com.musicbox.server.packets.outgoing.RedirectToVKPacket;
import com.musicbox.server.packets.outgoing.TokenPacket;
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
                @NotNull TokenPacket packet = new TokenPacket();

                String token = MD5.getMD5(oauth.getAccess_token()) +
                        MD5.getMD5(oauth.getUser_id() + UUID.randomUUID().toString());

                packet.setToken(token);
                Session session = Connection.getSession();

                VkontakteClient vkontakteClient = new VkontakteClient(oauth);

                LoginTokenEntity loginTokenEntity = new LoginTokenEntity();
                loginTokenEntity.setoAuthToken(oauth);
                loginTokenEntity.setToken(token);

                UserEntity userEntity = null;//(UserEntity) session.createQuery("from UserEntity where profile.id = :id").setParameter("id", vkontakteClient.getProfile().getId()).uniqueResult();


                if (userEntity == null) {
                    session.clear();

                    userEntity = new UserEntity();
                    userEntity.setProfile(vkontakteClient.getProfile());
                    userEntity.setName(userEntity.getProfile().getFirst_name());
                }

                loginTokenEntity.setUser(userEntity);

                session.saveOrUpdate(userEntity);
                session.flush();
                session.close();
                logintokens_.put(token, oauth);

                packet.send(connection);
            } else {
                System.out.println("ERROR!" + oauth.getError() + oauth.getError_description());
            }
        } else {
            (new RedirectToVKPacket()).send(connection);
        }
    }
}
