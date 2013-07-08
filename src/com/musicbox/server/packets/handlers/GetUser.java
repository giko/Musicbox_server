package com.musicbox.server.packets.handlers;

import com.musicbox.model.UserEntity;
import com.musicbox.server.MusicboxServer;
import com.musicbox.server.db.Connection;
import com.musicbox.server.packets.Packets;
import com.musicbox.server.packets.outgoing.CriticalErrorPacket;
import com.musicbox.server.packets.outgoing.UserPacket;
import org.webbitserver.WebSocketConnection;

import javax.persistence.EntityManager;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 3/25/13
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetUser extends AbstractHandler {
    public GetUser(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(WebSocketConnection connection, Packets.Incoming incoming) {
        EntityManager entityManager = Connection.getEntityManager();
        UserEntity user = null;
        try {
            user = (UserEntity) entityManager.createQuery("from UserEntity where id = :id").setParameter("id", Integer.valueOf(incoming.getMessage())).getSingleResult();
            (new UserPacket(user)).send(connection);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            (new CriticalErrorPacket("No such user!")).send(connection);
        }

        if (user == null) {
            (new CriticalErrorPacket("No such user!")).send(connection);
            throw new RuntimeException("No user found for id: ".concat(incoming.getMessage()));
        }
    }
}
