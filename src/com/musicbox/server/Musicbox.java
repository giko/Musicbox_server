package com.musicbox.server;

import com.google.gson.Gson;
import com.musicbox.server.Packets.Incoming;
import com.musicbox.server.Packets.Outgoing;
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

	@Override
	public void onMessage(WebSocketConnection connection, String msg)
			throws Exception {
		Incoming incoming = json.fromJson(msg, Incoming.class);
		Outgoing packet = new Outgoing();
		switch (incoming.getAction()) {
		case LOGIN:
			login(connection, incoming.getLoginUsername());
			break;
		case SEARCH:
			packet.setAction(Outgoing.Action.SEARCHRESULT);
			packet.setResult(wclient.Search(incoming.getMessage()));
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

	private void login(WebSocketConnection connection, String username) {
		connection.data(USERNAME_KEY, username); // associate username with
													// connection

		Outgoing outgoing = new Outgoing();
		outgoing.setAction(Outgoing.Action.JOIN);
		outgoing.setUsername(username);
		broadcast(outgoing);
	}

	private void broadcast(Outgoing packet) {
		String jsonStr = this.json.toJson(packet);
		for (WebSocketConnection connection : connections) {
			if (connection.data(USERNAME_KEY) != null) { // only broadcast to
															// those who have
															// completed login
				connection.send(jsonStr);
			}
		}
	}

	@Override
	public void onOpen(WebSocketConnection connection) throws Exception {
		connections.add(connection);
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
