package com.musicbox.server;

import com.google.gson.Gson;
import com.musicbox.lastfm.LastFmClient;
import com.musicbox.server.Packets.Incoming;
import com.musicbox.server.Packets.Outgoing;
import com.musicbox.vkontakte.OAuthToken;
import com.musicbox.vkontakte.VkontakteClient;
import com.musicbox.vkontakte.structure.profiles.Profile;
import com.musicbox.weborama.WeboramaClient;
import com.musicbox.weborama.structure.TrackList;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Musicbox extends BaseWebSocketHandler {

    private final Gson json = new Gson();
    private static final WeboramaClient wclient = new WeboramaClient();
    private static final LastFmClient lfclient = new LastFmClient();

    private HashMap<WebSocketConnection, Profile> connections = new HashMap<WebSocketConnection, Profile>();

    public static final String USERNAME_KEY = "vktoken";

    @Override
    public void onMessage(WebSocketConnection connection, String msg)
            throws Exception {
        Incoming incoming = json.fromJson(msg, Incoming.class);
        Outgoing packet = new Outgoing();
        switch (incoming.getAction()) {
            case LOGINBYTOKEN:
                login(connection, incoming.getMessage());
                break;
            case LOGINBYCODE:
                prelogin(connection, incoming.getMessage());
                break;
            case SEARCH:
                packet.setAction(Outgoing.Action.SEARCHRESULT);
                packet.setArtists(lfclient.SearchArtist(incoming.getMessage()));
                connection.send(this.json.toJson(packet));
                break;
            case GETURLBYTRACK:
                VkontakteClient vkclient = new VkontakteClient(connections.get(connection).getToken());
                packet.setAction(Outgoing.Action.SONGURL);
                packet.setMessage(vkclient.getURLByTrack(incoming.getMessage()));
                connection.send(this.json.toJson(packet));
                break;
            case GETSONGBYID:
                List<TrackList> song = new ArrayList<TrackList>();
                song.add(wclient.GetTrackBySongIdentifier(incoming.getMessage()));

                packet.setAction(Outgoing.Action.SONGS);
                packet.setSongs(song);
                connection.send(this.json.toJson(packet));
                break;
        }
    }

    private void prelogin(WebSocketConnection connection, String code) {
        OAuthToken oauth = VkontakteClient.getOauthTokenByCode(code);
        if (oauth.getError() == null || oauth.getError().equals("")) {
            Outgoing packet = new Outgoing(Packets.Outgoing.Action.TOKEN);
            packet.setMessage(this.json.toJson(oauth));
            connection.send(this.json.toJson(packet));
        }
    }

    private void login(WebSocketConnection connection, String token) {
        connection.data(USERNAME_KEY, token); // associate username with
        // connection
        OAuthToken oauth = this.json.fromJson(token, OAuthToken.class);
        if (oauth.getError() == null) {
            VkontakteClient vkclient = new VkontakteClient(oauth);

            connections.put(connection, vkclient.getProfile());
            Outgoing packet = new Outgoing(Packets.Outgoing.Action.MESSAGE);
            packet.setMessage("Welcome ".concat(connections.get(connection)
                    .getFirst_name()));
            System.out.println(connections.get(connection).getPhoto_big());
            connection.send(json.toJson(packet));
            System.out.println(vkclient.getURLByTrack("Metallica"));
        } else {
            connection.close();
        }
    }

    private void broadcast(Outgoing packet) {
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
    public void onOpen(WebSocketConnection connection) throws Exception {
        connections.put(connection, null);
    }

    @Override
    public void onClose(WebSocketConnection connection) throws Exception {
        if ((String) connection.data(USERNAME_KEY) != null) {
            Outgoing outgoing = new Outgoing(Outgoing.Action.LEAVE);
            outgoing.setUsername(connections.get(connection).getFirst_name());
            broadcast(outgoing);
        }
        connections.remove(connection);
    }
}
