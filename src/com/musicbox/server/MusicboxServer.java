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

    public static final String USERNAME_KEY = "vktoken";

    public static String getUsernameKey() {
        return USERNAME_KEY;
    }

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
    }

    public HashMap<WebSocketConnection, Profile> getConnections() {
        return connections;
    }

    @Override
    public void onMessage(@NotNull WebSocketConnection connection, @NotNull String msg)
            throws Exception {
        Incoming incoming = json.fromJson(msg, Incoming.class);
        if (packethandlers.containsKey(incoming.getAction())) {
            packethandlers.get(incoming.getAction()).HandlePacket(connection, incoming);
        } else {
            //Ошибка, пакета нет
        }
    }


    public void broadcast(@NotNull Outgoing packet) {
        String jsonStr = this.json.toJson(packet);
        for (WebSocketConnection connection : new ArrayList<WebSocketConnection>(
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
            Outgoing outgoing = new Outgoing(Outgoing.Action.LEAVE);
            //           outgoing.setUsername(connections.get(connection).getFirst_name());
            broadcast(outgoing);
        }
        connections.remove(connection);
    }
}
