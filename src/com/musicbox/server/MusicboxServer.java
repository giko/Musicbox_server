package com.musicbox.server;

import com.google.gson.Gson;
import com.musicbox.server.packets.Packets;
import com.musicbox.server.packets.handlers.*;
import com.musicbox.vkontakte.OAuthToken;
import com.musicbox.vkontakte.structure.profiles.Profile;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MusicboxServer extends BaseWebSocketHandler {
    @NotNull
    public static final String USERNAME_KEY = "vktoken";
    @NotNull
    private static final HashMap<String, OAuthToken> logintokens = new HashMap<String, OAuthToken>();
    @NotNull
    private final Gson json = new Gson();
    @NotNull
    private final HashMap<WebSocketConnection, Profile> connections = new HashMap<WebSocketConnection, Profile>();
    @NotNull
    private final HashMap<Packets.Incoming.Action, AbstractHandler> packethandlers = new HashMap<Packets.Incoming.Action, AbstractHandler>();

    public MusicboxServer() {

        packethandlers.put(Packets.Incoming.Action.LOGIN, new Login(this));
        packethandlers.put(Packets.Incoming.Action.LOGINBYCODE, new LoginByCode(this));
        packethandlers.put(Packets.Incoming.Action.SEARCH, new Search(this));
        packethandlers.put(Packets.Incoming.Action.GETURLBYTRACK, new GetUrlByTrack(this));
        packethandlers.put(Packets.Incoming.Action.CHATMESSAGE, new ChatMessage(this));
        packethandlers.put(Packets.Incoming.Action.GETTOPSONGSBYARTISTID, new GetTopSongsByArtistId(this));
        packethandlers.put(Packets.Incoming.Action.GETTOPSONGSBYARTISTNAME, new GetTopSongsByArtistName(this));
        packethandlers.put(Packets.Incoming.Action.ADDTOLIBRARY, new AddToLibrary(this));
        packethandlers.put(Packets.Incoming.Action.SEARCHBYTAG, new SearchByTag(this));
        packethandlers.put(Packets.Incoming.Action.GETAUDIOBYTRACK, new GetAudioByTrack(this));
        packethandlers.put(Packets.Incoming.Action.SEARCHSIMILARARTISTSBYNAME, new SearchSimilarArtistsByName(this));
        packethandlers.put(Packets.Incoming.Action.EXECUTEREQUESTRESULT, new ExecuteRequestResult(this));
    }

    @NotNull
    public static String getUsernameKey() {
        return USERNAME_KEY;
    }

    @NotNull
    public static HashMap<String, OAuthToken> getLogintokens() {
        return logintokens;
    }

    @NotNull
    public HashMap<WebSocketConnection, Profile> getConnections() {
        return connections;
    }

    @Override
    public void onMessage(@NotNull final WebSocketConnection connection, @NotNull String msg)
            throws Exception {

        if (!connections.containsKey(connection) && msg.length() > 100) {
            return;
        }

        final Packets.Incoming incoming = json.fromJson(msg, Packets.Incoming.class);

        if (incoming.getAction() == null || !connections.containsKey(connection) &&
                incoming.getAction() != Packets.Incoming.Action.LOGIN
                && incoming.getAction() != Packets.Incoming.Action.LOGINBYCODE) {
            //Someone is really smart
            return;
        }

        if (packethandlers.containsKey(incoming.getAction())) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    packethandlers.get(incoming.getAction()).HandlePacket(connection, incoming);
                }
            };
            thread.setDaemon(true);
            thread.start();
        } else {
            throw new RuntimeException(("No Handler for " + incoming.getAction().toString()) + " packet in packethandlers");
        }
    }

    public void broadcast(@NotNull Packets.Outgoing packet) {
        String jsonStr = this.json.toJson(packet);
        for (@NotNull WebSocketConnection connection : new ArrayList<WebSocketConnection>(
                connections.keySet())) {
            if (connection.data(USERNAME_KEY) != null) {
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
            Packets.Outgoing outgoing = new Packets.Outgoing(Packets.Outgoing.Action.LEAVE);
            outgoing.setMessage(connections.get(connection).getFirst_name());
            broadcast(outgoing);
        }
        connections.remove(connection);
    }

    @NotNull
    public Map<Packets.Incoming.Action, AbstractHandler> getPackethandlers() {
        return packethandlers;
    }
}
