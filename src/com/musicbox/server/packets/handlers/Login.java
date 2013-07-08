package com.musicbox.server.packets.handlers;

import com.musicbox.model.LoginTokenEntity;
import com.musicbox.model.vkontakte.VkontakteClient;
import com.musicbox.model.vkontakte.structure.profiles.Profile;
import com.musicbox.server.Config;
import com.musicbox.server.MusicboxServer;
import com.musicbox.server.db.Connection;
import com.musicbox.server.packets.Packets;
import com.musicbox.server.packets.outgoing.CriticalErrorPacket;
import com.musicbox.server.packets.outgoing.LoginSuccessPacket;
import com.musicbox.server.packets.outgoing.RedirectToVKPacket;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.WebSocketConnection;

import javax.persistence.EntityManager;
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

                if (current_profile != null && current_profile.getId() == vkclient.getProfile().getId()) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                connections_.put(connection, vkclient.getProfile());
                (new LoginSuccessPacket()).send(connection);
            } else {
                (new CriticalErrorPacket("Already Logged in")).send(connection);
            }
        } else {
            EntityManager entityManager = Connection.getEntityManager();
            try {
                LoginTokenEntity loginTokenEntity = (LoginTokenEntity) entityManager.createQuery("from LoginTokenEntity where token = :token").setParameter("token", token).getSingleResult();

                @NotNull VkontakteClient vkclient = new VkontakteClient(loginTokenEntity.getoAuthToken());

                Profile profile = loginTokenEntity.getoAuthToken().getProfile() == null ? vkclient.getProfile() : loginTokenEntity.getoAuthToken().getProfile();
                connections_.put(connection, profile);

                loginTokenEntity.getoAuthToken().setProfile(profile);
                ((Session) entityManager.getDelegate()).flush();

                (new LoginSuccessPacket()).send(connection);
            } catch (Exception e) {
                (new RedirectToVKPacket()).send(connection);
            }
        }
    }
}
