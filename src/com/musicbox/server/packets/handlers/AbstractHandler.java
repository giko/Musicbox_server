package com.musicbox.server.packets.handlers;

import com.musicbox.lastfm.LastFmClient;
import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets;
import com.musicbox.vkontakte.OAuthToken;
import com.musicbox.vkontakte.structure.profiles.Profile;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.WebSocketConnection;

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

    public abstract void HandlePacket(WebSocketConnection connection, Packets.Incoming incoming);

    public void HandleExecuteRequest(@NotNull WebSocketConnection connection, @NotNull String result) {
        throw new RuntimeException("Unimplemented HandleExecuteRequest in " + this.getClass().getName());
    }
}
