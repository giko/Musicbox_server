package com.musicbox.server.packets.handlers;

import com.musicbox.lastfm.LastFmClient;
import com.musicbox.server.MusicboxServer;
import com.musicbox.server.packets.Packets.Incoming;
import com.musicbox.vkontakte.OAuthToken;
import com.musicbox.vkontakte.structure.profiles.Profile;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.WebSocketConnection;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: giko
 * Date: 23.02.12
 * Time: 9:54
 */
public abstract class AbstractHandler {
    protected static MusicboxServer server;
    protected static String USERNAME_KEY;
    @NotNull
    protected static HashMap<WebSocketConnection, Profile> connections = new HashMap<WebSocketConnection, Profile>();
    @NotNull
    protected static HashMap<String, OAuthToken> logintokens = new HashMap<String, OAuthToken>();
    @NotNull
    protected static final LastFmClient lfclient = new LastFmClient();

    protected AbstractHandler(MusicboxServer server) {
        this.server = server;
        this.connections = this.server.getConnections();
        this.logintokens = this.server.getLogintokens();
        this.USERNAME_KEY = this.server.USERNAME_KEY;
    }

    public abstract void HandlePacket(WebSocketConnection connection, Incoming incoming);
    public void HandleExecuteRequest(WebSocketConnection connection, String result){
        return;
    }
}
