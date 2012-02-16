package com.musicbox.server;

import com.google.gson.Gson;
import com.musicbox.Packets.Incoming;
import com.musicbox.Packets.Outgoing;
import com.musicbox.weborama.WeboramaClient;
import com.musicbox.weborama.structure.TrackList;

import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Musicbox extends BaseWebSocketHandler {

	private final Gson json = new Gson();
	private static WeboramaClient wclient = new WeboramaClient();
	private Set<WebSocketConnection> connections = new HashSet<WebSocketConnection>();

	public static final String USERNAME_KEY = "username";

    private void login(WebSocketConnection connection, String username) {
        connection.data(USERNAME_KEY, username); // associate username with connection

        Outgoing outgoing = new Outgoing();
        outgoing.setAction(Outgoing.Action.JOIN);
        outgoing.setUsername(username);
        broadcast(outgoing);
    }

	@Override
	public void onOpen(WebSocketConnection connection) throws Exception {
		connections.add(connection);
	}

	@Override
	public void onMessage(WebSocketConnection connection, String msg)
			throws Exception {
		Incoming incoming = json.fromJson(msg, Incoming.class);
		switch (incoming.getAction()) {
		case LOGIN:
			login(connection, incoming.getLoginUsername());
			break;
		case SEARCH:
			Outgoing packet = new Outgoing();
			packet.setAction(Outgoing.Action.SEARCHRESULT);
			packet.setResult(wclient.Search(incoming.getMessage()));
			connection.send(this.json.toJson(packet));
			break;
		case GETSONGBYID:
			Outgoing song_packet = new Outgoing();
			song_packet.setAction(Outgoing.Action.SONGS);
			List<TrackList> song = new ArrayList<TrackList>();
			song.add(wclient.GetTrackBySongIdentifier(incoming.getMessage()));
			song_packet.setTrackList(song);
			connection.send(this.json.toJson(song_packet));
			break;
		}
	}
	
    private void broadcast(Outgoing outgoing) {
        String jsonStr = this.json.toJson(outgoing);
        for (WebSocketConnection connection : connections) {
            if (connection.data(USERNAME_KEY) != null) { // only broadcast to those who have completed login
                connection.send(jsonStr);
            }
        }
    }

    
	@Override
	public void onClose(WebSocketConnection connection) throws Exception {
		String username = (String) connection.data(USERNAME_KEY);
		if (username != null) {
			Outgoing outgoing = new Outgoing();
			outgoing.setAction(Outgoing.Action.LEAVE);
			outgoing.setUsername(username);
			broadcast(outgoing);
		}
		connections.remove(connection);
	}
}
