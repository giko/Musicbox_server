package com.musicbox.server.packets.handlers;

import com.musicbox.model.lastfm.LastFmClient;
import com.musicbox.model.vkontakte.OAuthToken;
import com.musicbox.model.vkontakte.structure.profiles.Profile;
import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.WebSocketConnection;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 23.02.12
 * Time: 9:54
 */
public abstract class AbstractHandler {
    protected static MusicboxServer server_;

    @NotNull
    protected static Map<WebSocketConnection, Profile> connections_ = new HashMap<WebSocketConnection, Profile>();
    @NotNull
    protected static HashMap<String, OAuthToken> logintokens_ = new HashMap<String, OAuthToken>();
    @NotNull
    protected static final LastFmClient lfclient = new LastFmClient();

    protected AbstractHandler(MusicboxServer server) {
        server_ = server;
        connections_ = this.server_.getConnections();
        logintokens_ = this.server_.getLogintokens();
    }

    String getIpByConnection(WebSocketConnection connection) {
        Field messageEvent = null;
        Field remoteAddress = null;
        String ip = null;

        try {
            messageEvent = connection.httpRequest().getClass().getDeclaredField("messageEvent");
            messageEvent.setAccessible(true);

            remoteAddress = UpstreamMessageEvent.class.getDeclaredField("remoteAddress");
            remoteAddress.setAccessible(true);

            try {
                ip = ((InetSocketAddress) remoteAddress.get(messageEvent.get(connection.httpRequest()))).getAddress().toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }

        return ip;
    }

    public abstract void HandlePacket(WebSocketConnection connection, Packets.Incoming incoming);

    public void HandleExecuteRequest(@NotNull WebSocketConnection connection, @NotNull String result) {
        throw new RuntimeException("Unimplemented HandleExecuteRequest in " + this.getClass().getName());
    }
}
