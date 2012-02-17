package com.musicbox.server;

import com.google.gson.Gson;
import com.musicbox.WebWorker;
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

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Musicbox extends BaseWebSocketHandler {

	private final Gson json = new Gson();
	private static WeboramaClient wclient = new WeboramaClient();
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

	private void prelogin(WebSocketConnection connection, String code) {
		String query = "https://oauth.vkontakte.ru/access_token?client_id=2810768&client_secret=OP1L2XAhJHfgEHg8Y1Vu&code="
				.concat(code);
		Gson json = new Gson();
		OAuthToken token = json.fromJson(
				new InputStreamReader(WebWorker.retrieveStream(query)),
				OAuthToken.class);
		if (token.getError() == null || token.getError() == "") {
			Outgoing packet = new Outgoing();
			packet.setAction(Packets.Outgoing.Action.TOKEN);
			packet.setMessage(this.json.toJson(token));
			connection.send(this.json.toJson(packet));
		}
	}

	private void login(WebSocketConnection connection, String token) {
		connection.data(USERNAME_KEY, token); // associate username with
												// connection
		VkontakteClient vkclient = new VkontakteClient();
		
		connections.put(connection, vkclient.getProfileById(json.fromJson(token,OAuthToken.class).getUser_id(),json.fromJson(token,OAuthToken.class).getAccess_token()));
		Outgoing packet = new Outgoing(Packets.Outgoing.Action.MESSAGE);
		packet.setMessage("Welcome ".concat(connections.get(connection).getFirst_name()));
		System.out.println(connections.get(connection).getPhoto_big());
		connection.send(json.toJson(packet));
	}

	private void broadcast(Outgoing packet) {
		String jsonStr = this.json.toJson(packet);
		for (WebSocketConnection connection : new ArrayList<WebSocketConnection>(connections.keySet())) {
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
