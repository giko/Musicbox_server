package com.musicbox.server.packets.handlers;

import com.musicbox.server.MusicboxServer;
import com.musicbox.server.logic.tools.MD5;
import com.musicbox.server.packets.Packets;
import com.musicbox.vkontakte.OAuthToken;
import com.musicbox.vkontakte.VkontakteClient;
import org.webbitserver.WebSocketConnection;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 23.02.12
 * Time: 10:44
 */
public class LoginByCode extends AbstractHandler{

    public LoginByCode(MusicboxServer server) {
        super(server);
    }

    @Override
    public void HandlePacket(WebSocketConnection connection, Packets.Incoming incoming) {
        String code = incoming.getMessage();
        if (!code.equals("")) {
            OAuthToken oauth = VkontakteClient.getOauthTokenByCode(code);
            if (oauth.getError() == null || oauth.getError().equals("")) {
                Packets.Outgoing packet = new Packets.Outgoing(Packets.Outgoing.Action.TOKEN);
                packet.setMessage(MD5.getMD5(oauth.getAccess_token()) + MD5.getMD5(oauth.getUser_id() + " go%d"));
                logintokens.put(MD5.getMD5(oauth.getAccess_token()) + MD5.getMD5(oauth.getUser_id() + " go%d"), oauth);
                connection.send(packet.toJson());
            }
        } else {
            connection.send(new Packets.Outgoing(Packets.Outgoing.Action.REDIRECTTOVK).toJson());
        }
    }
}
