package com.musicbox.server;

import com.google.gson.Gson;
import com.musicbox.lastfm.LastFmClient;
import com.musicbox.server.Packets.Incoming;
import com.musicbox.server.Packets.Outgoing;
import com.musicbox.server.logic.tools.MD5;
import com.musicbox.vkontakte.OAuthToken;
import com.musicbox.vkontakte.VkontakteClient;
import com.musicbox.vkontakte.structure.profiles.Profile;
import com.musicbox.weborama.WeboramaClient;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;
import java.util.HashMap;

public class Musicbox extends BaseWebSocketHandler {

    @NotNull
    private final Gson json = new Gson();
    @NotNull
    private static final WeboramaClient wclient = new WeboramaClient();
    @NotNull
    private static final LastFmClient lfclient = new LastFmClient();
    @NotNull
    private static final HashMap<String, OAuthToken> logintokens = new HashMap<String, OAuthToken>();
    @NotNull
    private final HashMap<WebSocketConnection, Profile> connections = new HashMap<WebSocketConnection, Profile>();

    @NotNull
    public static final String USERNAME_KEY = "vktoken";

    @Override
    public void onMessage(@NotNull WebSocketConnection connection, @NotNull String msg)
            throws Exception {
        Incoming incoming = json.fromJson(msg, Incoming.class);
        Outgoing packet = new Outgoing();
        switch (incoming.getAction()) {
            case LOGIN:
                login(connection, incoming.getMessage());
                break;
            case LOGINBYCODE:
                prelogin(connection, incoming.getMessage());
                break;
            case SEARCH:
                packet.setAction(Outgoing.Action.SEARCHRESULT);
                packet.setArtists(lfclient.SearchArtist(incoming.getMessage().trim()));
                connection.send(this.json.toJson(packet));
                break;
            case GETURLBYTRACK:
                VkontakteClient vkclient = new VkontakteClient(connections.get(connection).getToken());
                packet.setAction(Outgoing.Action.SONGURL);
                packet.setMessage(vkclient.getURLByTrack(incoming.getMessage()));
                connection.send(this.json.toJson(packet));
                break;
            case GETSONGBYID:
                break;
            case CHATMESSAGE:
                packet.setAction(Outgoing.Action.MESSAGE);
                packet.setMessage(connections.get(connection).getFirst_name().concat(" ")
                        .concat(connections.get(connection).getLast_name())
                        .concat(" написал: ").concat(incoming.getMessage()));
                broadcast(packet);
                break;
            case GETTOPSONGSBYARTISTID:
                packet.setAction(Outgoing.Action.SONGS);
                packet.setSongs(lfclient.getTopTracksByArtistID(incoming.getMessage()));
                connection.send(this.json.toJson(packet));
                break;
            case GETTOPSONGSBYARTISTNAME:
                packet.setAction(Outgoing.Action.SONGS);
                packet.setSongs(lfclient.getTopTracksByArtistName(incoming.getMessage()));
                connection.send(this.json.toJson(packet));
                break;
        }
    }

    private void prelogin(@NotNull WebSocketConnection connection, @NotNull String code) {
        if (!code.equals("")) {
            OAuthToken oauth = VkontakteClient.getOauthTokenByCode(code);
            if (oauth.getError() == null || oauth.getError().equals("")) {
                Outgoing packet = new Outgoing(Packets.Outgoing.Action.TOKEN);
                packet.setMessage(MD5.getMD5(oauth.getAccess_token()) + MD5.getMD5(oauth.getUser_id() + " go%d"));
                logintokens.put(MD5.getMD5(oauth.getAccess_token()) + MD5.getMD5(oauth.getUser_id() + " go%d"), oauth);
                connection.send(this.json.toJson(packet));
            }
        } else {
            connection.send(json.toJson(new Outgoing(Outgoing.Action.REDIRECTTOVK)));
        }
    }

    private void login(@NotNull WebSocketConnection connection, @NotNull String token) {
        if (logintokens.containsKey(token)) {
            connection.data(USERNAME_KEY, logintokens.get(token).getUser_id());
            VkontakteClient vkclient = new VkontakteClient(logintokens.get(token));
            connections.put(connection, vkclient.getProfile());
            Outgoing packet = new Outgoing(Packets.Outgoing.Action.MESSAGE);
            packet.setMessage("Welcome ".concat(connections.get(connection)
                    .getFirst_name()));
            connection.send(json.toJson(packet));
            packet = new Outgoing(Outgoing.Action.SEARCHRESULT);
            packet.setArtists(lfclient.getTopArtists());
            connection.send(this.json.toJson(packet));
        } else {
            connection.send(json.toJson(new Outgoing(Outgoing.Action.REDIRECTTOVK)));
        }
    }

    private void broadcast(@NotNull Outgoing packet) {
        String jsonStr = this.json.toJson(packet);
        for (WebSocketConnection connection : new ArrayList<WebSocketConnection>(
                connections.keySet())) {
            if (connection.data(USERNAME_KEY) != null) { // only broadcast to
                // those who have
                // completed login
                connection.send(jsonStr);
            }
        }
    }

    @Override
    public void onOpen(@NotNull WebSocketConnection connection) throws Exception {
        connections.put(connection, null);
    }

    @Override
    public void onClose(@NotNull WebSocketConnection connection) throws Exception {
        if (connection.data(USERNAME_KEY) != null) {
            Outgoing outgoing = new Outgoing(Outgoing.Action.LEAVE);
            outgoing.setUsername(connections.get(connection).getFirst_name());
            broadcast(outgoing);
        }
        connections.remove(connection);
    }
}
