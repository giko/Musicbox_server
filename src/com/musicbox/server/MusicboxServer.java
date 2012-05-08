package com.musicbox.server;

import com.google.gson.Gson;
import com.musicbox.server.packets.Packets.Incoming;
import com.musicbox.server.packets.Packets.Outgoing;
import com.musicbox.server.packets.handlers.*;
import com.musicbox.vkontakte.OAuthToken;
import com.musicbox.vkontakte.structure.profiles.Profile;
import org.jetbrains.annotations.NotNull;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;
import java.util.HashMap;

public class MusicboxServer extends BaseWebSocketHandler {

    @NotNull
    private final Gson json = new Gson();
    @NotNull
    private static final HashMap<String, OAuthToken> logintokens = new HashMap<String, OAuthToken>();
    @NotNull
    private final HashMap<WebSocketConnection, Profile> connections = new HashMap<WebSocketConnection, Profile>();
    @NotNull
    private final HashMap<Incoming.Action, AbstractHandler> packethandlers = new HashMap<Incoming.Action, AbstractHandler>();

    @NotNull
    public static final String USERNAME_KEY = "vktoken";

    @NotNull
    public static String getUsernameKey() {
        return USERNAME_KEY;
    }

    @NotNull
    public static HashMap<String, OAuthToken> getLogintokens() {
        return logintokens;
    }

    public MusicboxServer() {
        packethandlers.put(Incoming.Action.LOGIN, new Login(this));
        packethandlers.put(Incoming.Action.LOGINBYCODE, new LoginByCode(this));
        packethandlers.put(Incoming.Action.SEARCH, new Search(this));
        packethandlers.put(Incoming.Action.GETURLBYTRACK, new GetUrlByTrack(this));
        packethandlers.put(Incoming.Action.CHATMESSAGE, new ChatMessage(this));
        packethandlers.put(Incoming.Action.GETTOPSONGSBYARTISTID, new GetTopSongsByArtistId(this));
        packethandlers.put(Incoming.Action.GETTOPSONGSBYARTISTNAME, new GetTopSongsByArtistName(this));
        packethandlers.put(Incoming.Action.ADDTOLIBRARY, new AddToLibrary(this));
        packethandlers.put(Incoming.Action.SEARCHBYTAG, new SearchByTag(this));
        packethandlers.put(Incoming.Action.GETAUDIOBYTRACK, new GetAudioByTrack(this));
        packethandlers.put(Incoming.Action.SEARCHSIMILARARTISTSBYNAME, new SearchSimilarArtistsByName(this));
        packethandlers.put(Incoming.Action.EXECUTEREQUESTRESULT, new ExecuteRequestResult(this));
    }

    @NotNull
    public HashMap<WebSocketConnection, Profile> getConnections() {
        return connections;
    }

    @Override
    public void onMessage(@NotNull final WebSocketConnection connection, @NotNull String msg)
            throws Exception {
        final Incoming incoming = json.fromJson(msg, Incoming.class);

        if (incoming.getAction() == null || (connections.get(connection) == null &&
                (incoming.getAction() != Incoming.Action.LOGIN
                        && incoming.getAction() != Incoming.Action.LOGINBYCODE))) {
            //Someone is realy smart
            return;
        }

        if (packethandlers.containsKey(incoming.getAction())) {
            @NotNull Thread thread = new Thread() {
                @Override
                public void run() {
                    packethandlers.get(incoming.getAction()).HandlePacket(connection, incoming);
                }
            };
            thread.setDaemon(true);
            thread.start();
        } else {
            //Ошибка, пакета нет
        }
    }


    public void broadcast(@NotNull Outgoing packet) {
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
        connection.send((new Outgoing(Outgoing.Action.MESSAGE, "bla")).toJson());
    }

    @Override
    public void onClose(@NotNull WebSocketConnection connection) throws Exception {
        if (connection.data(USERNAME_KEY) != null) {
            @NotNull Outgoing outgoing = new Outgoing(Outgoing.Action.LEAVE);
            outgoing.setMessage(connections.get(connection).getFirst_name());
            broadcast(outgoing);
        }
        connections.remove(connection);
    }

    @NotNull
    public HashMap<Incoming.Action, AbstractHandler> getPackethandlers() {
        return packethandlers;
    }
}
